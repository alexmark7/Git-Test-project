# Min debugging i IntelliJ IDEA

1. Jeg satte et breakpoint på `battleshipGame.setupBoard()` og startede programmet i debug mode.
2. Med Step Into gik jeg ind i `setupBoard()`, sprang print-kaldet over med Step Over og gik ind i `Board`-konstruktøren med Step Into.
3. Jeg brugte Step Over frem til `addBattleship()`, hvor jeg mistænkte fejlen, som gjorde skibene vandrette.
4. Med Step Into og Step Over fulgte jeg programmet frem til `new Random().nextInt(maxValue)`.
5. Med Evaluate Expression så jeg, at funktionen altid gav `0`. Jeg tilføjede `+1` til grænsen og brugte Step Out til at gå tilbage.
6. Jeg satte derefter et breakpoint på `battleshipGame.play()` for at undersøge de tre træffere. Undervejs opstod også en `ArrayIndexOutOfBoundsException`.
7. Med Step Into og Step Over kom jeg frem til `isSunk()`, hvor jeg satte et betinget breakpoint med `hitCount == 3`.
8. Her så jeg, at `hitCount > BATTLESHIP_LENGTH` var forkert, og ændrede betingelsen til `hitCount >= BATTLESHIP_LENGTH`.
9. Til sidst åbnede jeg oversigten over breakpoints og fjernede dem alle.
