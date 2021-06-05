## Afhankelijkheden 

1. `npm install -g azure-functions-core-tools@3 --unsafe-perm true`
1. Installeer VS Code
1. Installeer vervolgens de Azure Functions-extensie voor Visual Studio Code
1. Zodra de extensie is geïnstalleerd, klikt u op het Azure-logo in de activiteitenbalk. Klik bij Azure: Functions op Aanmelden bij Azure... en volg de instructies op het scherm.

## Lokaal draaien
Druk op F5 om de functie-app uit te voeren. Tijdens runtime wordt een URL gegenereerd voor een willekeurige HTTP-functie. Deze URL kunt u kopiëren en uitvoeren in de adresbalk van de browser. Als u wilt stoppen met fouten opsporen, drukt u op Shift + F5.

## Deployen
Klik op het pictogram Implementeren in functie-app… (^) in het paneel Azure: Functions. Wanneer u wordt gevraagd om een functie-app te selecteren, kiest u cinq-connect.

## Zonder VS code ##
`npm install -g azure-functions-core-tools@3 --unsafe-perm true`

### Lokaal draaien

`mvn package azure-functions:run`

of

`func host start` (in target\azure-functions\cinq-connect directory)

### Deployen

`mvn azure-functions:deploy`

### Remote debug

1. `npm install -g dbgproxy` (legt een tunnel naar de serverless functie)
1. installeer az commandline
1. remote debug naar localhost:8898
