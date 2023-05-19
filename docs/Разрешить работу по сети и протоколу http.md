# Разрешить работу по сети и протоколу http

## разрешения для сетевого взаимодейтсвия

Добавляем разрешения в манифест

```xml
<!-- Разрешения для сетевого взаимодействия. -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## разрешить http

Т.к http небезопасный трафик, по умолчанию обмен по нему запрещен. Чтобы включить, нужно прописать активацию в манифесте


```xml
<application
	...
    android:usesCleartextTraffic="true"
    ...
</application>
```
