package com.example.pr;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.core.app.ActivityCompat;

public class MainActivity extends Activity implements View.OnClickListener {

    //Экземпляры классов наших кнопок
    Button redButton,greenButton,blueButton,whiteButton;


    //Сокет, с помощью которого мы будем отправлять данные на Arduino
    BluetoothSocket clientSocket;

    //Эта функция запускается автоматически при запуске приложения
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        redButton = findViewById(R.id.button3);
        greenButton = findViewById(R.id.button4);
        blueButton = findViewById(R.id.button3);
        whiteButton = findViewById(R.id.button4);
        //Добавлем "слушатель нажатий" к кнопке
        redButton.setOnClickListener(this);
        greenButton.setOnClickListener(this);
        blueButton.setOnClickListener(this);
        whiteButton.setOnClickListener(this);


        String enableBT = BluetoothAdapter.ACTION_REQUEST_ENABLE;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivityForResult(new Intent(enableBT), 0);

        //Мы хотим использовать тот bluetooth-адаптер, который задается по умолчанию
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

        //Пытаемся проделать эти действия
        try{
            //Устройство с данным адресом - наш Bluetooth Bee
            //Адрес опредеяется следующим образом: установите соединение
            //между ПК и модулем (пин: 1234), а затем посмотрите в настройках
            //соединения адрес модуля. Скорее всего он будет аналогичным.
            BluetoothDevice device = bluetooth.getRemoteDevice("00:19:08:00:10:52");

            //Инициируем соединение с устройством
            Method m = device.getClass().getMethod(
                    "createRfcommSocket", new Class[] {int.class});

            clientSocket = (BluetoothSocket) m.invoke(device, 1);
            clientSocket.connect();

            //В случае появления любых ошибок, выводим в лог сообщение
        } catch (IOException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (SecurityException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (IllegalAccessException e) {
            Log.d("BLUETOOTH", e.getMessage());
        } catch (InvocationTargetException e) {
            Log.d("BLUETOOTH", e.getMessage());
        }

        //Выводим сообщение об успешном подключении
        Toast.makeText(getApplicationContext(), "CONNECTED", Toast.LENGTH_LONG).show();

    }



    //Как раз эта функция и будет вызываться

    @Override
    public void onClick(View v) {

        //Пытаемся послать данные
        try {
            //Получаем выходной поток для передачи данных
            OutputStream outStream = clientSocket.getOutputStream();

            int value = 0;

            //В зависимости от того, какая кнопка была нажата,
            //изменяем данные для посылки
            if (v == redButton) {

            }
            else if (v == greenButton) {

            }

            //Пишем данные в выходной поток
            outStream.write(value);

        } catch (IOException e) {
            //Если есть ошибки, выводим их в лог
            Log.d("BLUETOOTH", e.getMessage());
        }
    }
}