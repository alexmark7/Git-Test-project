# Anvendte debuggingteknikker i jdb

- Jeg satte breakpoints i `calculateEndPoint()` og `isSunk()` for at standse ved de mistænkte fejl.
- Med Step Into fulgte jeg kaldet til `getRandomInteger()` og så, at den øvre grænse var `1`.
- Med Step Out og Step Over fulgte jeg returværdien og så, at `switch` valgte den vandrette gren.
- Jeg inspicerede variablerne og konstaterede, at både `hitCount` og skibets længde var `3`.
- Jeg evaluerede betingelserne og så, at `3 > 3` gav `false`, mens `3 >= 3` gav `true`.
- Med Set Value ændrede jeg startpunktet til `(8,0)` for at fremkalde kantfejlen reproducerbart.
- Et exception breakpoint standsede ved arrayfejlen, hvor `x = 10` lå uden for brættets indeks `0–9`.
- Jeg brugte call stack til at følge fejlen tilbage gennem `addBattleship()` til konstruktøren.
