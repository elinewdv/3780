# Jumper: A Mobile App for Dietary Self Management

> **Disclaimer:** This is a prototype, not a medical advide. It is developed for **informational and educational purposes only**. The app should not replace profssional medical advices, diagnosis or treatment.

## About the prototype 
This mobilde application was designed and tested exclusively by our team as part of the INF-3780 course in Advanced Health Informatics as UiT. while we strived for accuracy and usability, the system remains in a prototype phase with limited testing and user validation.

The **Jumper** integrates a food tracking system and educational advice on nutrition and blood sugar regulation. It includes functionality for meal logging, food database lookup and advice presentation through a chatbot interface.

**Important Considerations** 
- The user is responsible for their own decsision regarding diet and diabetes management
- This app does not provide real-time glucose monitoring and should not be relied on for urgent health decisions
- Food and nutrition values are based on public data sources and may not reflect individual medical needs.
- No personal data is stored with encryption in this prototype; use it your discreation and risk.

## Why do we need Jumper?

Patients with diabetes or obesity often lack accessible tools to track their nutritional intake accurately. Existing apps may be overwhelming or lack support for local foods and habits.  **Jumper** aims to:
- Simplify food logging with context aware search
- Offer expertie advice and suggestions based on meal history
- Help users recognize patterns that affect blood sugar
- Provide a Norwegian focused food database using trusted APIs

## How to run Jumper ?

**Step 1.** Requirements : 
  - **Android :** https://developer.android.com/studio
  - **Github repository :** https://github.com/elinewdv/3780.git
    
**Step 2.** Clone the GitHub repository :
  - Open Android Studio
  - Select **Get from VCS**
  - Paste the GitHub URL

**Step 3.** Synchronize Gradle :
Once the project is open :
  - Go to **File > Sync Project with Gradle Files**

**Step 4.** Configure an Emulator :
Set up an emulator :
  - Go to **Tools > Device Manager**
  - Click **Create Device**
  - Choose a phone (e.g., Pixel 6) > Next
  - Choose a system image (API 30–34) > Download if needed > Next
  - Finish setup and launch the emulator

**Step 5.** Run the App :
  - Make sure the **app module** is selected in the top Run configuration dropdown
  - Choose your emulator or device from the device list
  - Click the **Run** button ▶️

**Troubleshooting**
  - No modules appear in Run configuration :
      - Check that settings.gradle includes the module (e.g., ':app')
      - Ensure the app folder has a valid build.gradle with :
        plugins {
          id 'com.android.application'
        }
      - Rebuild the project : **Build > Rebuild Project**
  - Gradle sync fails :
      - Try **File > Invalidate Caches / Restart**
      - Use correct **JDK version 17**
    

  ## Key features
  - **Meal Tracking** - Log meals and snacks with nutritional breakdown
  - **Food Database Integrations** - Uses Matvaretabellen API to retrieve food values
  - **Advice Page** - Educational content and chatbot suggestions

## Project Poster 
![Jumper Poster](jumper%20plakat.png)

## Technologies Used

- Kotlin + Android Studio
- Jetpack Compose
- Room Database
- kotlinx.serialization
- Figma
- Nettskjema

## Contributors
- Alan Demirbas
- Marius Pierre Joseph Le Gouallec
- Océane Guennec
- Océane Saqué
- Anna Eline Wefring de Vito
