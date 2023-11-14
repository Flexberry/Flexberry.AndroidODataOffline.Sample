# Flexberry.AndroidODataOffline.Sample

Возможности демонстрационного приложения:

- Отображение списка (list-форма).

- Возможность создавать и редактировать объекты (edit-форма).

- Обмен данными с ember-odata-backend по протоколу Odata.

- Работа с оффлайн БД.

- Поддержка режимов оффлайн/онлайн с логикой выбора одной из БД и синхронизации данных между ними.


Схема интерфейса приложения:

![image](https://github.com/Flexberry/Flexberry.AndroidODataOffline.Sample/assets/13151962/9ec0e80a-7042-469e-a611-f3a2efb8af46)


Схема работы приложения:

![image](https://github.com/Flexberry/Flexberry.AndroidODataOffline.Sample/assets/13151962/0ae01370-0404-4a56-a3a2-1635b012fe6f)


Схема архитектуры приложения:
![image](https://github.com/Flexberry/Flexberry.AndroidODataOffline.Sample/assets/13151962/a6de056b-20d4-4a77-95e8-7c447443ba5a)


Выбранный язык: Kotlin

IDE: Android Studio Flamingo (https://developer.android.com/studio)

UI: JetpackCompose (https://developer.android.com/jetpack/compose/tutorial)

DI: Hilt (https://developer.android.com/training/dependency-injection/hilt-android)

OfflineDataBase: Room (https://developer.android.com/training/data-storage/room)

OnlineOdataBackend: http://stands-backend.flexberry.net/odata (объекты Master и Detail)

