# Debugging – WS1

Opgaven er fra [MichaelViuff/WS1 – Debugging](https://github.com/MichaelViuff/WS1/tree/main/Debugging).
Det krævede projekt, [tkgregory/debug-demo](https://github.com/tkgregory/debug-demo), er klonet.
Udgangspunktet er commit `423a4bd5b79e9ad375da071f8dfeca3d9192c2c9` på `master`.

## De tre fejl

| Opgave | Årsag | Rettelse |
| --- | --- | --- |
| Kun vandrette skibe | `nextInt(1)` giver altid `0`, så `case 1` aldrig vælges. | Vælg retning med `getRandomInteger(2)`. |
| Ingen `Ship sunk!` efter tre træffere | `hitCount > 3` er falsk, når tælleren er præcis `3`. | Brug `hitCount >= BATTLESHIP_LENGTH`. |
| Sporadisk `ArrayIndexOutOfBoundsException` | Et lovligt startpunkt kan give et slutpunkt uden for brættet. | Afvis hele placeringen og træk et nyt startpunkt og en ny retning, før der skrives i arrayet. |

### 1. Retningen

`Random.nextInt(bound)` medtager `0`, men udelader `bound`.
Derfor har `nextInt(1)` kun ét muligt resultat: `0`.
I originalkoden bliver `getHorizontalEndPoint()` derfor altid valgt.
Efter rettelsen er både `0` og `1` mulige, så begge retninger kan vælges.

I den faktiske debuggerkørsel blev der sat breakpoint i `calculateEndPoint()`.
Step Into viste argumentet `maxValue = 1` i `getRandomInteger()`.
En separat evaluering af `new java.util.Random().nextInt(maxValue)` gav `0`.
Step Out og Step Over førte derefter til `case 0`.
Se [debuggerloggen](Logs/01-orientation-jdb.txt).

### 2. Sænkning

Skibet fylder tre felter. `Player` undgår at angribe samme felt igen, så tre
forskellige træffere betyder, at hele skibet er ramt.
Den oprindelige betingelse krævede mindst fire træffere.
Spillet fortsatte derfor efter den tredje træffer og endte med `Ship survived!`.

Et lille diagnoseprogram opstillede et kendt skib og ramte felterne `(0,0)`,
`(1,0)` og `(2,0)`. Ved breakpoint i originalens `isSunk()` var `hitCount = 3`,
men metoden returnerede `false`.
Se [debuggerloggen](Logs/02-sinking-jdb.txt) og [diagnoseprogrammet](OriginalProbe.java).
Diagnoseprogrammet bruger reflection til at opstille dette afgrænsede scenarie;
det er ikke en ændring af spillets produktionskode.

### 3. Placering uden for brættet

Et bræt på 10 × 10 har gyldige indeks fra `0` til `9`.
Et vandret skib med startpunkt `(8,0)` får slutpunkt `(10,0)`, fordi
`8 + 3 - 1 = 10`. Den indlejrede løkke forsøger derfor at skrive til `grid[10][0]`.

For at fremkalde fejlen reproducerbart blev `startPoint` sat til `(8,0)` i debuggeren,
inden slutpunktet blev beregnet. Et exception breakpoint standsede på
`grid[x][y] = true`, hvor `x = 10`, `y = 0`, `endPoint.x = 10` og `grid.length = 10`.
Call stack viste kæden fra konstruktøren til `addBattleship()`.
Se [debuggerloggen](Logs/03-bounds-jdb.txt).

Rettelsen kontrollerer begge koordinater, før nogen skibsfelter skrives:
`endPoint.x >= grid.length || endPoint.y >= grid.length`.
Er betingelsen sand, prøver en `do-while`-løkke en ny placering.
Kontrol af negative slutkoordinater er ikke nødvendig her: startkoordinaterne
er ikke-negative, og skibet vokser kun mod større x eller større y.
Et bræt mindre end skibets længde afvises med `IllegalArgumentException`,
så løkken ikke kan blive ved med at lede efter en umulig placering.

Fejlen skjules ikke med `try-catch`, og skibet afkortes ikke ved kanten.
Der placeres stadig præcis ét sammenhængende skib med tre felter.

## Ekstra fejl: sidste række og kolonne

Både `Board.randomGridPoint()` og `Player.randomGridPoint()` brugte størrelsen
minus én som øvre grænse. `nextInt(9)` giver kun `0`–`8`, så koordinaten `9`
blev udelukket på et bræt med størrelse `10`.
Begge metoder bruger nu hele brættets størrelse som eksklusiv øvre grænse.
Spilleren kan dermed også ramme skibe på sidste række og kolonne.

## Testbarhed og afgrænsning

De oprindelige offentlige konstruktører er bevaret.
Der er tilføjet konstruktører med package-adgang, som tager en `Random`.
Spillet bruger stadig almindelig tilfældighed; testene kan bruge kendte seeds
eller en fast sekvens. Det gør især kantfejlene reproducerbare.
En `Random` genbruges pr. objekt i stedet for at oprette en for hvert tal.
Spillets 50 ture, brættets størrelse og den automatiske spiller er bevaret.

Spillets eksisterende aftaler gælder fortsat: `Board.attack()` kaldes med gyldige
koordinater, og `Player` sikrer forskellige angreb. Der er ikke indført en ny
manuel inputfunktion eller en generel API til vilkårlige angreb.

## Verifikation

Der er 15 JUnit-tests i `BoardTest` og `PlayerTest`:

- Begge retninger placeres korrekt.
- Sænkning sker ved præcis tre forskellige træffere; missere ændrer ikke dette.
- Begge retninger virker ved nederste/højre kant.
- Ugyldige vandrette og lodrette placeringer forsøges igen.
- Mindste gyldige bræt virker, og for små brætter afvises.
- 5.000 brætter med kendte seeds har præcis tre sammenhængende skibsfelter.
- Sidste række/kolonne kan angribes, og tidligere angreb gentages ikke.
- Alle 100 felter kan angribes én gang uden at sidde fast.
- 1.000 seedede spilforløb overholder højst 50 ture og sænker kun efter tre træffere.
- `BattleshipGame.play()` udskriver `Ship sunk!` og tager ingen ekstra tur, når brættet er sænket.

De 1.000 seedede forløb tester samspillet mellem `Player` og `Board` med spillets
samme stopbetingelse. Outputtesten tester særskilt den rigtige `play()`-metode
med et allerede sænket bræt. Der er desuden kørt rigtige spil fra `main()`.
Se de faktiske kørselsresultater i [Logs](Logs).

Projektets eksisterende Java 17-toolchain og Gradle 8.0-wrapper er bevaret.
Kørslerne her er udført med OpenJDK 17.0.20. Der er ikke ændret build-system
alene for at løse debugging-opgaven.

## Dokumentationsstatus

Debuggerlogs er fra faktiske `jdb`-sessioner mod originalkoden og viser
breakpoints, Step Into/Out/Over, evaluering, variabler, call stack og exception breakpoint.
`jdb` er JDK'ets kommandolinjedebugger; der er ikke udført en IntelliJ-session her.

**IntelliJ-screenshots mangler fortsat.** Opgaven kræver disse som løbende dokumentation.
Mappen [Screenshots](../Screenshots) indeholder den korte README på otte linjer
og en konkret [vejledning](../Screenshots/VEJLEDNING.md) til at tage dem.
README'en beskriver det faktisk udførte arbejde og skal opdateres, når billederne er taget.

## Afleveringskontrol

- [x] Det angivne `debug-demo`-projekt er klonet.
- [x] De tre fejl er forklaret og rettet.
- [x] Den ekstra fejl i koordinatgrænsen er rettet i begge klasser.
- [x] Rettelserne er afprøvet med 15 beståede tests og faktiske debuggerkørsler.
- [x] `Screenshots/README.MD` indeholder 5–10 linjer.
- [ ] De faktiske IntelliJ-screenshots er taget og lagt i `Screenshots`.
- [ ] Screenshots og den opdaterede korte README er committed og pushed.
- [ ] Repositorylinket er indsendt i Itslearning.

Løsningen er forberedt med AI-assistance. De vedlagte logs og tests dokumenterer
arbejdet udført her; de dokumenterer ikke en personlig IntelliJ-session.
