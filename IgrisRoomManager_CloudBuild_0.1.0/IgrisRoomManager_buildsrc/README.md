# Igris Room Manager – Android-Prototyp 0.1.0

Ein erster nativer Android-Projektstart mit Kotlin und Jetpack Compose.

## Bereits enthalten
- Dunkles App-Design
- Startseite und Navigation
- Raumname, Breite und Länge in Metern
- Automatische Berechnung der Raumfläche
- 2D-Grundriss-Prototyp mit verschiebbarem Sofa
- 2D-/3D-Umschalter (3D derzeit als Platzhalter)
- Möbel-Demokatalog inklusive eigener Möbel-Platzhalter
- Budget-Seite als Grundgerüst

## Noch nicht enthalten
- Persistente Speicherung (Datenbank)
- Maßstabsgetreue, frei skalierbare Möbelbibliothek
- Türen, Fenster, Heizkörper und mehrere Räume
- echte 3D-Engine
- Live-Produktdaten von IKEA oder OTTO
- Export als PDF/Bild und APK-Release

## Öffnen
1. Android Studio installieren.
2. Dieses Projekt entpacken.
3. In Android Studio `Open` wählen und den Projektordner öffnen.
4. Gradle-Synchronisierung abwarten.
5. Emulator oder Android-Gerät verbinden und `Run` drücken.

Voraussetzungen: Android Studio mit JDK 17, Android SDK 35 und Internet für den ersten Gradle-Download.

## Technische Richtung
- Kotlin + Jetpack Compose
- Room Database für lokale Projekte (nächster Schritt)
- 2D-Editor mit Compose Canvas
- 3D-Ansicht als separates Modul, später z. B. mit einer passenden Android-3D-Engine

Dies ist ein Entwicklungsstart und wurde hier nicht in einer Android-SDK-Umgebung kompiliert oder auf einem Gerät getestet.
