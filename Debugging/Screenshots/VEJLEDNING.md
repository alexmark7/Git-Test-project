# Tag de faktiske IntelliJ-screenshots

Denne mappe har endnu ingen billeder. Nedenstående er en fremgangsmåde til
ægte screenshots fra din egen debugger-session.

Åbn selve `Debugging`-projektmappen, hvis du bruger GitHub-versionen i
`Git-Test-project`. Åbn `debug-demo`, hvis du bruger den selvstændige ZIP.
Importér som Gradle-projekt og vælg JDK 17 som Gradle JVM og Project SDK.
Vent, til testafhængighederne er hentet.

## Billeder fra den rettede kode

Kør de nævnte JUnit-metoder med **Debug**, så scenarierne er reproducerbare.
De kræver ingen ændring af produktionskoden.
Brug metode-/kodelinjerne nedenfor som pejlemærker; linjenumre kan flytte sig.

| Filnavn | Test og breakpoint | Hvad billedet skal vise |
| --- | --- | --- |
| `01-Lodret-skib.png` | `BoardTest.placesVerticalShip()`; breakpoint på `return new Point(...)` i `getVerticalEndPoint()` | `startPoint = (0,0)`, den lodrette gren og call stack. Brug Step Into fra `calculateEndPoint()` for at følge valget. |
| `02-Tre-traeffere.png` | `BoardTest.sinksAfterExactlyThreeDistinctHits()`; breakpoint på return-linjen i `isSunk()` med betingelsen `hitCount == 3` | `hitCount = 3` og Evaluate Expression `hitCount >= 3`, der giver `true`. |
| `03-Ugyldig-placering.png` | `BoardTest.retriesHorizontalPlacementThatWouldLeaveBoard()`; breakpoint på `while (endPoint.x >= ...)` | Første forsøg har start `(8,0)`, slut `(10,0)` og størrelse `10`. Step Over viser, at der vælges om før skrivning i arrayet. |
| `04-Gyldig-placering.png` | Samme test; fortsæt til næste gennemløb af kontrollen | Nyt startpunkt `(7,0)`, slutpunkt `(9,0)` og en falsk afvisningsbetingelse. |
| `05-Alle-tests.png` | Kør hele testpakken | IntelliJ viser 15 beståede tests. |

Et breakpoint er på linjen **før** dens instruktion udføres.
Undgå at evaluere `random.nextInt(...)` på testens egen `Random`: det bruger
et tal fra sekvensen og ændrer forløbet. Brug en separat `new java.util.Random()`
til demonstrationer, eller inspicér returværdien med Step Out.

## Vis også originalens fejl

For at dokumentere selve fejlsøgningen bør du også tage billeder fra originalen.
Fra et selvstændigt klon af debug-demo kan originalen åbnes i en separat worktree:

`git worktree add --detach ../debug-demo-original 423a4bd5b79e9ad375da071f8dfeca3d9192c2c9`

Bruger du kun GitHub-mappen fra Git-Test-project, klon først
`https://github.com/tkgregory/debug-demo.git` til en separat mappe.
Åbn originalmappen i et andet IntelliJ-vindue.
Kopiér `Documentation/OriginalProbe.java` fra løsningen til originalprojektets
`src/test/java/com/tomgregory/OriginalProbe.java`.
Det er et diagnoseprogram, der kører mod de uændrede originale klasser.
Opret en Application-run configuration for `com.tomgregory.OriginalProbe` med
testmodulets classpath og et af argumenterne nedenfor.

1. **Retning – argument `orientation`:** Breakpoint i `calculateEndPoint()` på switch-linjen. Step Into til `getRandomInteger()` viser `maxValue = 1`. Step Out/Over viser `case 0`. Gem som `06-Original-retning.png`.
2. **Sænkning – argument `sinking`:** Breakpoint på return-linjen i `isSunk()`. Variabler viser `hitCount = 3`; Evaluate Expression `hitCount > 3` giver `false`. Diagnoseprogrammet opstiller et kendt skib for at isolere fejlen. Gem som `07-Original-saenkning.png`.
3. **Arrayfejl – argument `bounds`:** Breakpoint i `addBattleship()` før `calculateEndPoint(startPoint)`. Brug Set Value på startpunktets felter: `x = 8`, `y = 0`. Tilføj et Java Exception Breakpoint for `java.lang.ArrayIndexOutOfBoundsException` under View Breakpoints, og fortsæt. Debuggeren stopper ved `grid[x][y] = true`, hvor `x = 10` og `grid.length = 10`. Vis også call stack. Gem som `08-Original-arrayfejl.png`.

Tag billederne under arbejdet og læg dem i denne mappe.
Opdatér sidste linje i `README.MD`, så den præcist beskriver din egen IntelliJ-session.
Commit og push derefter billeder og README til løsningsbranchen.
Indsend linket til `Debugging`-mappen på denne branch, så underviseren ser den rigtige opgave.
