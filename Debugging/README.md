# WS1 – Debugging

Rettet løsning til [opgaven fra MichaelViuff](https://github.com/MichaelViuff/WS1/tree/main/Debugging),
baseret på et klon af [tkgregory/debug-demo](https://github.com/tkgregory/debug-demo).

De tre opgavefejl er rettet: begge skibsretninger kan vælges, tre træffere sænker
skibet, og placeringer uden for brættet afvises før skrivning i arrayet.
En ekstra fejl, der udelukkede sidste række og kolonne, er også rettet.

**Status:** Kode og 15 tests er færdige. Gradle-build er bestået.
IntelliJ-screenshots skal stadig tages og tilføjes for at opfylde hele afleveringskravet.

## Kør projektet

Projektet bruger den oprindelige JDK 17-toolchain og Gradle 8.0-wrapper.
Åbn denne mappe som Gradle-projekt i IntelliJ, og kør `BattleshipGame.main()`.
Fra terminalen kan du køre `./gradlew clean test build` og derefter
`java -jar build/libs/debug-demo-1.0-SNAPSHOT.jar`.
På Windows bruges `gradlew.bat clean test build`.

Et normalt spil må gerne ende med `Ship survived!`, hvis færre end tre felter
rammes inden for 50 ture. Det er en del af spillets regler.

## Dokumentation

- [Fejlårsager, rettelser og testbeskrivelse](Documentation/README.md)
- [Faktiske debugger- og testlogs](Documentation/Logs)
- [Den korte README på otte linjer](Screenshots/README.MD)
- [Præcis vejledning til IntelliJ-screenshots](Screenshots/VEJLEDNING.md)
- [Original projektbeskrivelse](Documentation/README.upstream.md)

Der er brugt AI-assistance til rettelser og dokumentation.
Debuggerlogs er fra JDK'ets `jdb`; der er ikke vedlagt billeder fra en udført IntelliJ-session.
