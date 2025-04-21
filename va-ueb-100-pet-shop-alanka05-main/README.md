[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/sfZ1bPLw)
# slm-va-ueb-pet_shop

Vaadin GUI-Übung Pet-Shop

## UML

[![](https://img.plantuml.biz/plantuml/svg/lLHDQzj04BtlhzZSR8hSDaTXGbBJb41e0uBsK4ePQSVIDjAkEZl2k1__lMDznPPZarDrMMRcwSrxioDT1mRYfYgLkk02AzHr2TQfy6XT3GILpae8uOQPRDemtcNU9VONwZTJoCfILk9HhGPZhClrvHIVC19wtjQuUWVPOqw-SMRbIq_wIs4Pp-1llUCpo5tv8uYL15n8aEnw3x4wiFCCjB06DLOrRtM5LOeKkbJM-jjZIYUgcOH3IkXONTG4UGMw2NZdH7Y5nWBZWxm2EGQzXZAWsYCB1HY_sGsAgNbfbptVlggzarp2snArtEVb4VcKUMovN6ui5z-x-EfgYdlYFgMHC6DmkTp-YEWCFa7MlZH8f-kGiQUJ3Gmmf13mj89kGEIGz5kbLBi7-iRP2ihVIiiJQMlYU-_oFd9owt4YQp1cijry3rExROkp0gYFQx8PniOtgMn2cugYzdpLzvhD3yF5OZ6V03yWoqAyJjgZp-7FHkOnqpxz8QECFxM7dkjOfzwN2Ax7iU_apNQbNkdVmUq3oWtvMh_KhtoOX24S94ULFTdePuZ_fw5kXlSsy6UBczwN2134Fc15A9_MMSXxOyzpt7ggZW2gg-c8R5xmL_aB67JVegjX64Dz-ISQYrFHQX8VSKqzg2DJ9ruPF4qxFADcSAUkqHdvD_m3)](https://editor.plantuml.com/uml/lLHDQzj04BtlhzZSR8hSDaTXGbBJb41e0uBsK4ePQSVIDjAkEZl2k1__lMDznPPZarDrMMRcwSrxioDT1mRYfYgLkk02AzHr2TQfy6XT3GILpae8uOQPRDemtcNU9VONwZTJoCfILk9HhGPZhClrvHIVC19wtjQuUWVPOqw-SMRbIq_wIs4Pp-1llUCpo5tv8uYL15n8aEnw3x4wiFCCjB06DLOrRtM5LOeKkbJM-jjZIYUgcOH3IkXONTG4UGMw2NZdH7Y5nWBZWxm2EGQzXZAWsYCB1HY_sGsAgNbfbptVlggzarp2snArtEVb4VcKUMovN6ui5z-x-EfgYdlYFgMHC6DmkTp-YEWCFa7MlZH8f-kGiQUJ3Gmmf13mj89kGEIGz5kbLBi7-iRP2ihVIiiJQMlYU-_oFd9owt4YQp1cijry3rExROkp0gYFQx8PniOtgMn2cugYzdpLzvhD3yF5OZ6V03yWoqAyJjgZp-7FHkOnqpxz8QECFxM7dkjOfzwN2Ax7iU_apNQbNkdVmUq3oWtvMh_KhtoOX24S94ULFTdePuZ_fw5kXlSsy6UBczwN2134Fc15A9_MMSXxOyzpt7ggZW2gg-c8R5xmL_aB67JVegjX64Dz-ISQYrFHQX8VSKqzg2DJ9ruPF4qxFADcSAUkqHdvD_m3)


## Aufgabe - `AnimalListView`

- Implementieren Sie eine View zur Anzeige aller `Animal` Objekte
    - Zeigen Sie, bis auf die `id` alle Attribute in einem Grid an
    - Alle Spalten müssen sortierbar sein
- Die View soll aus dem Menü aufrufbar sein
- Fügen Sie dem Grid eine Spalte hinzu, die pro Zeile zwei Buttons anzeigt:
    - Delete
    - Edit


## Aufgabe - `AnimalListView`

- Erweitern Sie die View mit einer `onDelete` Methode
- Die Methode soll beim Drücken auf den Button `Delete` den Datensatz löschen
- Zeigen Sie dem Benutzer entweder eine Meldung über den Erfolg bzw. einen Fehler an


## Aufgabe - `AnimalCreateView`

- Implementieren Sie eine View zum Erfassen aller Daten (bis auf `id`) einer `Animal`-Instanz
- Die View soll aus dem Menü aufrufbar sein
- Erweitern Sie die Klasse `Animal` mit Annotations, zur Prüfung der Benutzereingabe
- Der Button `Cancel` navigiert zurück zur View in der die `Animal` Objekte angezeigt werden.
- Der Button `Save` navigiert, nach dem erfolgreichem Speichern, zurück zur `AnimalListView`
- Zeigen Sie dem Benutzer eine Meldung über den Erfolg an bzw. eine sprechende Fehlermeldung


## Aufgabe - `AnimalEditView`

- Implementieren Sie eine View in der bestehende `Animal`-Instanzen bearbeitet werden können
- Diese View darf *nicht* vom Menü aufrufbar sein
- Erweitern Sie die Methode `onEdit` in der Klasse `AnimalListView` mit einer Navigation
    - Beim Klick auf den `Button` im `Grid` soll mit der `id` des aktuellen `Animal` zur View `AnimalEditView` navigiert werden
    - Laden Sie mit der `id` den Datensatz aus dem Service und zeigen Sie die Daten in der UI an.
- `Cancel` bricht die Aktion ab und navigiert wieder zur List-View
- `Save` speichert die Änderungen und navigiert anschließend zur List-View
- Zeigen Sie dem Benutzer eine Meldung über den Erfolg an bzw. eine sprechende Fehlermeldung

## Aufgabe - `AnimalFormView`

- Implementieren Sie eine View, die sowohl neue Tiere anlegen kann, aber auch bestehende Tiere bearbeiten kann
- Die `route` der View muss einen optionalen Pfad-Parameter erhalten (mit *?*): ```animals/edit/:animalId?``` 
- Aufruf aus dem Menü: Übergeben Sie dem Binder in der `init`-Methode eine neue 
  `Animal`-Instanz: ```binder.setBean(new Animal())```
- Aufruf aus dem Grid mit Parameter: Laden Sie mit der `id` ein `Animal`-Objekt aus dem Service und rufen 
  sie die `setBean`-Methode am Binder auf ```binder.setBean(animal)``` 


## Aufgabe

- Implementieren Sie die oben angefürhten Aufgaben auch für die Klasse `PetShop`