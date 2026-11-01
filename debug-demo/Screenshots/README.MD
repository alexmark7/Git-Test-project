# Debuggingforløb med jdb

1. Jeg undersøgte først, hvorfor skibene kun blev placeret vandret, og satte et breakpoint i `calculateEndPoint()`.
2. Jeg brugte variabler og call stack til at orientere mig i programmets forløb.
3. Med Step Into fulgte jeg kaldet til `getRandomInteger()` og opdagede, at `nextInt(1)` altid gav `0`.
4. Med Step Out og Step Over fulgte jeg returværdien tilbage til `switch` og så den vandrette gren blive valgt.
5. Derefter satte jeg et breakpoint i `isSunk()` og undersøgte, hvorfor tre træffere ikke sænkede skibet.
6. Jeg inspicerede `hitCount` og evaluerede betingelsen: `3 > 3` gav `false`, mens `3 >= 3` gav `true`.
7. Til sidst fremkaldte jeg kantfejlen ved at sætte startpunktet til `(8,0)` og standsede med et exception breakpoint.
8. Jeg så, at indeks `10` lå uden for brættets indeks `0–9`, og brugte call stack til at finde fejlen i `addBattleship()`.
9. Efter rettelserne afprøvede jeg begge skibsretninger, sænkning efter tre træffere og placeringer ved brættets kanter.
