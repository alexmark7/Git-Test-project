# Debugging i IntelliJ IDEA

1. Jeg satte et breakpoint på `battleshipGame.setupBoard()` og brugte Step Into til at gå ind i `setupBoard()`.
2. Jeg sprang print-kaldet over med Step Over og brugte Step Into til at gå ind i `Board`-konstruktøren.
3. Med Step Over og Step Into fulgte jeg programmet gennem `addBattleship()` frem til `new Random().nextInt(maxValue)`.
4. Med Evaluate Expression opdagede jeg, at funktionen altid gav `0`. Jeg rettede den til `nextInt(maxValue + 1)` og brugte Step Out til at gå tilbage.
5. Derefter satte jeg et breakpoint på `battleshipGame.play()` og fulgte programmet med Step Into og Step Over frem til `isSunk()`.
6. Jeg satte et betinget breakpoint med `hitCount == 3` og så, at sammenligningen stadig gav `false`. Derfor ændrede jeg `>` til `>=`.
7. Jeg åbnede derefter oversigten over breakpoints og fjernede dem alle.
8. For at undersøge den tilbageværende indeksfejl tilføjede jeg et exception breakpoint for `java.lang.ArrayIndexOutOfBoundsException`.
9. Da debuggeren stoppede i `addBattleship()`, undersøgte jeg variablerne og så, at `y = 10` lå uden for brættets gyldige indeks `0–9`.
10. Jeg tilføjede en `do-while`-løkke, som vælger en ny placering, hvis skibets slutpunkt ligger uden for brættet. Derefter afprøvede jeg spillet igen, og det virkede.
