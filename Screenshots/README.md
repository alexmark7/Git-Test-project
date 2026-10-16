# WS1 – Command Line Interface (CLI)

Kommandoerne og screenshots er udført af Codex i et Linux-/Bash-miljø den 16. oktober 2026. De viser ikke arbejde udført på Alexanders egen computer.

Opgave: https://github.com/MichaelViuff/WS1/tree/main/Command%20Line%20Interface%20(CLI)

## Status

- Opgave 1: Alle 10 trin er udført. `Archieve` er tom, og `hello-kopi.txt` ligger i mappen over den med indholdet `Hello World`.
- Opgave 2: Den udleverede `CliDemo.java` er downloadet, kompileret og kørt uændret med OpenJDK 17.0.20. En byte-sammenligning med underviserens fil bekræfter, at indholdet er identisk. Der er kørt uden argumenter, med `-?` og med alle tre commands: `greet`, `sum` og `echo`.
- Opgave 3: Repository er klonet via CLI. Screenshots er samlet i `Screenshots` og committed lokalt. Push via CLI er forsøgt, men afvist på grund af manglende terminal-login. Screenshots er derefter uploadet via ChatGPTs GitHub-forbindelse. Dette opfylder uploaden, men erstatter ikke opgavens krav om et vellykket push via CLI. Dokumentation af det trin mangler endnu.

## Screenshots

| Fil | Dokumentation |
| --- | --- |
| 01-Grundlaeggende-CLI.png | Oprettelse, navigation, flytning, kopiering og sletning. |
| 02a-Java-kompilering.png | Download, Java-version, kompilering og kørsel uden argumenter. |
| 02b-Java-argumenter.png | Hjælp, `greet Alexander`, `sum 3 4.5 10` og `echo`. |
| 03-Git-CLI.png | Clone, oprettelse af Screenshots, flytning, commit og det afviste push. |

## Færdiggør CLI-dokumentationen fra din egen computer

Filerne ligger allerede på GitHub. Åbn terminalen i din lokale kopi af `Git-Test-project`, kontrollér med `git status`, at du ikke har lokale ændringer, og hent den nye aflevering:

```bash
git pull --ff-only
```

Tilføj en kort note i denne README om, at du selv har kontrolleret CLI-kørslen. Commit og push via terminalen:

```bash
git add Screenshots/README.md
git commit -m "Verify CLI workshop locally"
git push origin main
```

Tag et screenshot af de vellykkede kommandoer og gem det i `Screenshots` som `04-Git-push.png`. Tilføj filen og opdatér status ovenfor, før du laver et sidste commit og push:

```bash
git add Screenshots/
git commit -m "Document successful CLI workshop push"
git push origin main
```

Aflever repository-linket: https://github.com/alexmark7/Git-Test-project

## Kommandoerne fra opgave 1 og 2

Disse er Bash-kommandoer og kan køres på din egen Linux-computer fra en mappe, hvor `Modul - CLI` endnu ikke findes. Tag screenshots undervejs, hvis du vil dokumentere din egen udførelse.

```bash
mkdir "Modul - CLI"
cd "Modul - CLI"
echo "Hello World" > hello.txt
cat hello.txt
mkdir Archieve
mv hello.txt Archieve/
cd Archieve
cp hello.txt hello-kopi.txt
mv hello-kopi.txt ../
rm hello.txt
cd ..

curl -fLsS -o CliDemo.java 'https://raw.githubusercontent.com/MichaelViuff/WS1/main/Command%20Line%20Interface%20(CLI)/CliDemo.java'
javac CliDemo.java
java CliDemo
java CliDemo '-?'
java CliDemo greet Alexander
java CliDemo sum 3 4.5 10
java CliDemo echo 'Hello World'
```

`javac` skal være installeret som del af et JDK. Brug samme JDK-version til kompilering og kørsel.
