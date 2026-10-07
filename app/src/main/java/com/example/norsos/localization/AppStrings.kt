package com.example.norsos.localization

import com.example.norsos.model.AppLanguage

object AppStrings {

    fun appTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Norsos • Acil Durum"
        AppLanguage.EN -> "Norsos • Emergency SOS"
        AppLanguage.NO -> "Norsos • Nødsituasjon"
        AppLanguage.SV -> "Norsos • Nödsituation"
        AppLanguage.DA -> "Norsos • Nødsituation"
    }

    fun tabSos(lang: AppLanguage): String = "SOS"

    fun tabContacts(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Kişiler"
        AppLanguage.EN -> "Contacts"
        AppLanguage.NO -> "Kontakter"
        AppLanguage.SV -> "Kontakter"
        AppLanguage.DA -> "Kontakter"
    }

    fun tabTools(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Araçlar"
        AppLanguage.EN -> "Tools"
        AppLanguage.NO -> "Verktøy"
        AppLanguage.SV -> "Verktyg"
        AppLanguage.DA -> "Værktøjer"
    }

    fun tabSettings(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Ayarlar"
        AppLanguage.EN -> "Settings"
        AppLanguage.NO -> "Innstillinger"
        AppLanguage.SV -> "Inställningar"
        AppLanguage.DA -> "Indstillinger"
    }

    fun tabDataSafety(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Veri Güvenliği"
        AppLanguage.EN -> "Data Safety"
        AppLanguage.NO -> "Datasikkerhet"
        AppLanguage.SV -> "Datasäkerhet"
        AppLanguage.DA -> "Datasikkerhed"
    }

    fun testModeBanner(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "TEST MODU AKTİF: Gerçek arama veya SMS gönderilmez."
        AppLanguage.EN -> "TEST MODE ACTIVE: No real calls or SMS will be sent."
        AppLanguage.NO -> "TESTMODUS AKTIV: Ingen reelle anrop eller SMS sendes."
        AppLanguage.SV -> "TESTLÄGE AKTIVT: Inga riktiga samtal eller SMS skickas."
        AppLanguage.DA -> "TESTTILSTAND AKTIV: Ingen rigtige opkald eller SMS sendes."
    }

    fun activeEmergencyTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "ACİL DURUM AKTİF!"
        AppLanguage.EN -> "EMERGENCY ACTIVE!"
        AppLanguage.NO -> "NØDSITUASJON AKTIV!"
        AppLanguage.SV -> "NÖDSITUATION AKTIV!"
        AppLanguage.DA -> "NØDSITUATION AKTIV!"
    }

    fun activeEmergencyDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Siren, flaşör ve acil çağrı tetiklendi. Güvende olduğunuzda durdurun."
        AppLanguage.EN -> "Siren, strobe and emergency dispatch triggered. Stop when safe."
        AppLanguage.NO -> "Sirene, strobelys og nødvarsel utløst. Stopp når du er trygg."
        AppLanguage.SV -> "Siren, stroboskop och nödanrop utlösta. Stoppa när du är säker."
        AppLanguage.DA -> "Sirene, strobelys og nødopkald udløst. Stop når du er i sikkerhed."
    }

    fun stopEmergency(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Acil Durumu Durdur"
        AppLanguage.EN -> "Stop Emergency"
        AppLanguage.NO -> "Stopp nødsituasjon"
        AppLanguage.SV -> "Stoppa nödsituation"
        AppLanguage.DA -> "Stop nødsituation"
    }

    fun primaryContactLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "BİRİNCİL ACİL KİŞİ"
        AppLanguage.EN -> "PRIMARY EMERGENCY CONTACT"
        AppLanguage.NO -> "PRIMÆR NØDKONTAKT"
        AppLanguage.SV -> "PRIMÄR NÖDKONTAKT"
        AppLanguage.DA -> "PRIMÆR NØDKONTAKT"
    }

    fun quickToolsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Hızlı Güvenlik Araçları"
        AppLanguage.EN -> "Quick Safety Tools"
        AppLanguage.NO -> "Hurtige sikkerhetsverktøy"
        AppLanguage.SV -> "Snabba säkerhetsverktyg"
        AppLanguage.DA -> "Hurtige sikkerhedsværktøjer"
    }

    fun toolWhistle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Düdük"
        AppLanguage.EN -> "Whistle"
        AppLanguage.NO -> "Fløyte"
        AppLanguage.SV -> "Visselpipa"
        AppLanguage.DA -> "Fløjte"
    }

    fun toolSiren(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Siren"
        AppLanguage.EN -> "Siren"
        AppLanguage.NO -> "Sirene"
        AppLanguage.SV -> "Siren"
        AppLanguage.DA -> "Sirene"
    }

    fun toolStrobe(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Flaşör"
        AppLanguage.EN -> "Strobe"
        AppLanguage.NO -> "Strobe"
        AppLanguage.SV -> "Strobo"
        AppLanguage.DA -> "Strobe"
    }

    fun toolShare(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Paylaş"
        AppLanguage.EN -> "Share"
        AppLanguage.NO -> "Del"
        AppLanguage.SV -> "Dela"
        AppLanguage.DA -> "Del"
    }

    fun liveLocationReady(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Canlı Konum Hazır"
        AppLanguage.EN -> "Live Location Ready"
        AppLanguage.NO -> "Posisjon er klar"
        AppLanguage.SV -> "Positionen är redo"
        AppLanguage.DA -> "Placering er klar"
    }

    fun searchingGps(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "GPS Konumu Aranıyor..."
        AppLanguage.EN -> "Searching GPS..."
        AppLanguage.NO -> "Søker etter GPS..."
        AppLanguage.SV -> "Söker efter GPS..."
        AppLanguage.DA -> "Søger efter GPS..."
    }

    fun seconds(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "SANİYE"
        AppLanguage.EN -> "SECONDS"
        AppLanguage.NO -> "SEKUNDER"
        AppLanguage.SV -> "SEKUNDER"
        AppLanguage.DA -> "SEKUNDER"
    }

    fun stop(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "DURDUR"
        AppLanguage.EN -> "STOP"
        AppLanguage.NO -> "STOPP"
        AppLanguage.SV -> "STOPP"
        AppLanguage.DA -> "STOP"
    }

    fun sosSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "ACİL DURUM"
        AppLanguage.EN -> "EMERGENCY"
        AppLanguage.NO -> "NØDSITUASJON"
        AppLanguage.SV -> "NÖDSITUATION"
        AppLanguage.DA -> "NØDSITUATION"
    }

    fun cancelCountdown(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Geri Sayımı İptal Et"
        AppLanguage.EN -> "Cancel Countdown"
        AppLanguage.NO -> "Avbryt nedtelling"
        AppLanguage.SV -> "Avbryt nedräkning"
        AppLanguage.DA -> "Annuller nedtælling"
    }

    fun instructionIdle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Acil durumda butona dokunun"
        AppLanguage.EN -> "Tap button in an emergency"
        AppLanguage.NO -> "Trykk på knappen i en nødsituasjon"
        AppLanguage.SV -> "Tryck på knappen i en nödsituation"
        AppLanguage.DA -> "Tryk på knappen i en nødsituation"
    }

    fun instructionActive(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Durdurmak için butona dokunun"
        AppLanguage.EN -> "Tap button to stop"
        AppLanguage.NO -> "Trykk på knappen for å stoppe"
        AppLanguage.SV -> "Tryck på knappen för att stoppa"
        AppLanguage.DA -> "Tryk på knappen for at stoppe"
    }

    fun contactsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Acil Durum Kişileri"
        AppLanguage.EN -> "Emergency Contacts"
        AppLanguage.NO -> "Nødkontakter"
        AppLanguage.SV -> "Nödkontakter"
        AppLanguage.DA -> "Nødkontakter"
    }

    fun contactsSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Acil durumda bu kişilere otomatik SMS gönderilir ve birincil kişi aranır."
        AppLanguage.EN -> "In an emergency, automatic SMS is sent to these contacts and primary contact is called."
        AppLanguage.NO -> "I en nødsituasjon sendes automatisk SMS til disse og primærkontakt ringes."
        AppLanguage.SV -> "I en nödsituation skickas automatiskt SMS till dessa och primärkontakt rings upp."
        AppLanguage.DA -> "I en nødsituation sendes automatisk SMS til disse og primærkontakt ringes op."
    }

    fun noContactsYet(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Henüz acil kişi eklenmedi"
        AppLanguage.EN -> "No emergency contacts added yet"
        AppLanguage.NO -> "Ingen nødkontakter lagt til ennå"
        AppLanguage.SV -> "Inga nödkontakter har lagts till än"
        AppLanguage.DA -> "Ingen nødkontakter tilføjet endnu"
    }

    fun tapPlusToAdd(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Aşağıdaki '+' butonuna dokunarak kişi ekleyin."
        AppLanguage.EN -> "Tap the '+' button below to add a contact."
        AppLanguage.NO -> "Trykk på '+'-knappen nedenfor for å legge til."
        AppLanguage.SV -> "Tryck på '+'-knappen nedan för att lägga till."
        AppLanguage.DA -> "Tryk på '+'-knappen nedenfor for at tilføje."
    }

    fun makePrimary(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Birincil Yap"
        AppLanguage.EN -> "Set Primary"
        AppLanguage.NO -> "Sett som primær"
        AppLanguage.SV -> "Gör till primär"
        AppLanguage.DA -> "Gør til primær"
    }

    fun addNewContact(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Yeni Acil Kişi Ekle"
        AppLanguage.EN -> "Add Emergency Contact"
        AppLanguage.NO -> "Legg til nødkontakt"
        AppLanguage.SV -> "Lägg till nödkontakt"
        AppLanguage.DA -> "Tilføj nødkontakt"
    }

    fun editContact(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Kişiyi Düzenle"
        AppLanguage.EN -> "Edit Contact"
        AppLanguage.NO -> "Rediger kontakt"
        AppLanguage.SV -> "Redigera kontakt"
        AppLanguage.DA -> "Rediger kontakt"
    }

    fun nameLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Ad Soyad"
        AppLanguage.EN -> "Full Name"
        AppLanguage.NO -> "Navn"
        AppLanguage.SV -> "Namn"
        AppLanguage.DA -> "Navn"
    }

    fun phoneLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Telefon Numarası"
        AppLanguage.EN -> "Phone Number"
        AppLanguage.NO -> "Telefonnummer"
        AppLanguage.SV -> "Telefonnummer"
        AppLanguage.DA -> "Telefonnummer"
    }

    fun relationshipLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Yakınlık Derecesi (Anne, Eş, vb.)"
        AppLanguage.EN -> "Relationship (Mother, Spouse, etc.)"
        AppLanguage.NO -> "Relasjon (Mor, Ektefelle, etc.)"
        AppLanguage.SV -> "Relation (Mamma, Make/Maka, etc.)"
        AppLanguage.DA -> "Relation (Mor, Ægtefælle, etc.)"
    }

    fun setAsPrimaryLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Birincil Acil Kişi Olarak Ayarla"
        AppLanguage.EN -> "Set as Primary Emergency Contact"
        AppLanguage.NO -> "Sett som primær nødkontakt"
        AppLanguage.SV -> "Ställ in som primär nödkontakt"
        AppLanguage.DA -> "Indstil som primær nødkontakt"
    }

    fun save(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Kaydet"
        AppLanguage.EN -> "Save"
        AppLanguage.NO -> "Lagre"
        AppLanguage.SV -> "Spara"
        AppLanguage.DA -> "Gem"
    }

    fun cancel(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "İptal"
        AppLanguage.EN -> "Cancel"
        AppLanguage.NO -> "Avbryt"
        AppLanguage.SV -> "Avbryt"
        AppLanguage.DA -> "Annuller"
    }

    fun settingsTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Ayarlar & Yapılandırma"
        AppLanguage.EN -> "Settings & Preferences"
        AppLanguage.NO -> "Innstillinger"
        AppLanguage.SV -> "Inställningar"
        AppLanguage.DA -> "Indstillinger"
    }

    fun settingsSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Dil, SOS mesajı ve tetikleme davranışlarını özelleştirin."
        AppLanguage.EN -> "Customize language, SOS message and trigger behaviors."
        AppLanguage.NO -> "Tilpass språk, SOS-melding og utløseratferd."
        AppLanguage.SV -> "Anpassa språk, SOS-meddelande och beteenden."
        AppLanguage.DA -> "Tilpas sprog, SOS-besked og udløsningsadfærd."
    }

    fun languageSelectionTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Uygulama Dili"
        AppLanguage.EN -> "Application Language"
        AppLanguage.NO -> "App-språk"
        AppLanguage.SV -> "Applikationsspråk"
        AppLanguage.DA -> "App-sprog"
    }

    fun messageTemplateTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Acil Durum Mesaj Şablonu"
        AppLanguage.EN -> "Emergency Message Template"
        AppLanguage.NO -> "Mal for nødmelding"
        AppLanguage.SV -> "Mall för nödmeddelande"
        AppLanguage.DA -> "Skabelon til nødmeddelelse"
    }

    fun messageTemplateHint(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "{location} yazılan yere canlı konum bağlantısı eklenir."
        AppLanguage.EN -> "{location} will be replaced with live Google Maps link."
        AppLanguage.NO -> "{location} erstattes med aktiv posisjonslenke."
        AppLanguage.SV -> "{location} ersätts med live-positionslänk."
        AppLanguage.DA -> "{location} erstattes med live-placeringslink."
    }

    fun countdownTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Güvenlik Geri Sayım Süresi"
        AppLanguage.EN -> "Safety Countdown Duration"
        AppLanguage.NO -> "Sikkerhetsnedtelling"
        AppLanguage.SV -> "Säkerhetsnedräkning"
        AppLanguage.DA -> "Sikkerhedsnedtælling"
    }

    fun countdownDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Yanlışlıkla basmaları önlemek için butona basıldıktan sonraki bekleme süresi."
        AppLanguage.EN -> "Delay before trigger to prevent accidental presses."
        AppLanguage.NO -> "Forsinkelse før utløsning for å unngå feiltrykk."
        AppLanguage.SV -> "Fördröjning innan utlösning för att förhindra oavsiktliga tryck."
        AppLanguage.DA -> "Forsinkelse før udløsning for at forhindre utilsigtede tryk."
    }

    fun triggerBehaviors(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Tetikleme Davranışları"
        AppLanguage.EN -> "Trigger Behaviors"
        AppLanguage.NO -> "Utløserfunksjoner"
        AppLanguage.SV -> "Utlösarbeteenden"
        AppLanguage.DA -> "Udløseradfærd"
    }

    fun sirenAlarm(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Acil Siren Alarmı"
        AppLanguage.EN -> "Emergency Siren Alarm"
        AppLanguage.NO -> "Nødsirenealarm"
        AppLanguage.SV -> "Nödsirenalarm"
        AppLanguage.DA -> "Nødsirenealarm"
    }

    fun sirenAlarmDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "SOS anında yüksek desibel siren çalar"
        AppLanguage.EN -> "Plays high-decibel siren on SOS trigger"
        AppLanguage.NO -> "Spiller høy sirene ved SOS-utløsing"
        AppLanguage.SV -> "Spelar hög siren vid SOS-utlösning"
        AppLanguage.DA -> "Afspiller høj sirene ved SOS-udløsning"
    }

    fun strobeLight(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Flaşör / Fener Çakar"
        AppLanguage.EN -> "Strobe / Flashlight Flasher"
        AppLanguage.NO -> "Strobelys / Lommelykt"
        AppLanguage.SV -> "Stroboskop / Ficklampa"
        AppLanguage.DA -> "Strobelys / Lommelygte"
    }

    fun strobeLightDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Kamera flaşını dikkat çekecek şekilde yanıp söndürür"
        AppLanguage.EN -> "Flashes camera LED rapidly for high visibility"
        AppLanguage.NO -> "Blinker kameralys for høy synlighet"
        AppLanguage.SV -> "Blinkar kamerablixten för hög synlighet"
        AppLanguage.DA -> "Blinker kamerablitz for høj synlighed"
    }

    fun vibrationHaptic(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "SOS Haptik Titreşim"
        AppLanguage.EN -> "SOS Haptic Vibration"
        AppLanguage.NO -> "SOS Haptisk vibrasjon"
        AppLanguage.SV -> "SOS Haptisk vibration"
        AppLanguage.DA -> "SOS Haptisk vibration"
    }

    fun vibrationHapticDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Mors alfabesiyle (... --- ...) titreşim deseni"
        AppLanguage.EN -> "Morse code (... --- ...) vibration pattern"
        AppLanguage.NO -> "Morsekode (... --- ...) vibrasjonsmønster"
        AppLanguage.SV -> "Morsekod (... --- ...) vibrationsmönster"
        AppLanguage.DA -> "Morsekode (... --- ...) vibrationsmønster"
    }

    fun autoCallPrimary(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Birincil Kişiyi Otomatik Arama"
        AppLanguage.EN -> "Auto Call Primary Contact"
        AppLanguage.NO -> "Ring primærkontakt automatisk"
        AppLanguage.SV -> "Ring primärkontakt automatiskt"
        AppLanguage.DA -> "Ring til primærkontakt automatisk"
    }

    fun autoCallPrimaryDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Mesaj gönderiminden sonra birincil kişiyi arama ekranını açar"
        AppLanguage.EN -> "Opens dialer for primary contact after dispatch"
        AppLanguage.NO -> "Åpner anrop for primærkontakt etter varsel"
        AppLanguage.SV -> "Öppnar uppringare för primärkontakt efter larm"
        AppLanguage.DA -> "Åbner opkald for primærkontakt efter alarm"
    }

    fun testMode(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Güvenli Test Modu"
        AppLanguage.EN -> "Safe Test Mode"
        AppLanguage.NO -> "Sikker testmodus"
        AppLanguage.SV -> "Säkert testläge"
        AppLanguage.DA -> "Sikker testtilstand"
    }

    fun testModeDesc(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "Gerçek arama ve SMS yapılmaz. Buton ve sesleri güvenle deneyin."
        AppLanguage.EN -> "No real calls or SMS. Safely test buttons and sounds."
        AppLanguage.NO -> "Ingen reelle anrop/SMS. Test knapper og lyder trygt."
        AppLanguage.SV -> "Inga riktiga samtal/SMS. Testa knappar och ljud säkert."
        AppLanguage.DA -> "Ingen rigtige opkald/SMS. Test knapper og lyde sikkert."
    }

    fun defaultSosMessage(lang: AppLanguage): String = when (lang) {
        AppLanguage.TR -> "ACİL DURUM! Yardıma ihtiyacım var! Konumum: {location}"
        AppLanguage.EN -> "EMERGENCY! I need help! My location: {location}"
        AppLanguage.NO -> "NØDSITUASJON! Jeg trenger hjelp! Posisjonen min: {location}"
        AppLanguage.SV -> "NÖDSITUATION! Jag behöver hjälp! Min position: {location}"
        AppLanguage.DA -> "NØDSITUATION! Jeg har brug for hjælp! Min placering: {location}"
    }
}
