Opgaven undersøger tre fejl i Battleship-projektet fra tkgregory/debug-demo.
Der blev brugt breakpoints og Step Into til at undersøge valget af skibets retning.
Step Out og Step Over viste, at nextInt(1) altid førte til den vandrette gren.
Variabler og udtryk blev undersøgt ved tre træffere, hvor isSunk() fejlagtigt var false.
Et exception breakpoint og call stack viste adgang til indeks 10 på et bræt med størrelse 10.
Startpunktet blev sat til (8,0) i debuggeren for at fremkalde kantfejlen reproducerbart.
Rettelserne er kontrolleret med 15 JUnit-tests, 5.000 brætter og 1.000 seedede spilforløb.
Disse sessioner blev udført med jdb; IntelliJ-screenshots skal tilføjes efter en egen session.
