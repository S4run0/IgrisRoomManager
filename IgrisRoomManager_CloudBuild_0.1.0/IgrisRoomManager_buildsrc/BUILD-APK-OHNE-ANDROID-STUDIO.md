# APK bauen – ohne Android Studio auf deinem PC

Dieses Projekt enthält einen GitHub-Actions-Workflow, der die Debug-APK in der GitHub-Cloud baut.

## Einmalige Schritte
1. Auf https://github.com einloggen oder kostenlos registrieren.
2. Ein neues Repository erstellen, z. B. `IgrisRoomManager`.
3. Den Inhalt dieses ZIP-Archivs in das Repository hochladen (nicht den ZIP-Ordner als einzelne Datei).
4. Im Repository den Reiter **Actions** öffnen. Falls GitHub fragt, Workflows aktivieren.
5. Den Workflow **Build Android APK** auswählen und **Run workflow** starten.
6. Nach erfolgreichem Lauf den Eintrag öffnen und unter **Artifacts** `IgrisRoomManager-debug-apk` herunterladen.
7. ZIP entpacken. Darin liegt `app-debug.apk`.

## Auf dem Samsung Galaxy Z Flip6 installieren
1. APK auf das Handy übertragen und antippen.
2. Falls Android blockiert: Einstellungen öffnen und für die verwendete App (z. B. My Files/Dateien oder Chrome) **Unbekannte Apps installieren** erlauben.
3. Zur APK zurückkehren und **Installieren** wählen.

## Wichtig
- Das ist eine Debug-APK, keine im Play Store veröffentlichungsfertige Version.
- Der erste Cloud-Build kann wegen nicht verfügbarer Abhängigkeiten oder echter Quellcodefehler scheitern. Dann den Fehlertext aus GitHub Actions teilen.
- Der Prototyp ist funktional noch begrenzt; insbesondere ist 3D aktuell nur ein Platzhalter.
