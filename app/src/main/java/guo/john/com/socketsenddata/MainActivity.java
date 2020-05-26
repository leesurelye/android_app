package guo.john.com.socketsenddata;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import org.xmlpull.v1.XmlSerializer;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MainActivity extends AppCompatActivity {

    private EditText nameET;
    private EditText sexET;

    private Button sendjsonBTN;
    private Button sendXmlBTn;

    private SocketManager socket;
    private PrintWriter write;
    private Gson gson = new Gson();
    private Handler UIhandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取实列
                socket = SocketManager.getInstance();
                socket.initSocket(); // 初始化socket
                write = socket.getWrite();
            }
        }).start();
        findView();
    }

    /**
     * 查找控件
     */
    private void findView() {
        nameET = (EditText) findViewById(R.id.edittext1);
        sexET = (EditText) findViewById(R.id.edittext2);
        sendjsonBTN = (Button) findViewById(R.id.btn1);
        sendXmlBTn = (Button) findViewById(R.id.btn2);

        sendjsonBTN.setOnClickListener(sendjsonBTNClick);
       //todo list sendXmlBTn.setOnClickListener(sendXmlBTnClick);
    }

    /**
     * 发送json
     */
    private View.OnClickListener sendjsonBTNClick = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            String userName = nameET.getText().toString().trim();
            String sex = sexET.getText().toString().trim();
            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(MainActivity.this, "请输入用户名", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            if (TextUtils.isEmpty(sex)) {
                Toast.makeText(MainActivity.this, "请输入性别", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            UserInfo user = new UserInfo(userName, sex);
            final SendData data = new SendData(1, gson.toJson(user));
            new Thread(new Runnable() {

                @Override
                public void run() {
                    if (write != null) {
                        write.println(gson.toJson(data));
                        write.flush();
                        UIhandler.post(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "发送成功",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        }
    };

    /**
     * 发送xml
     */
    private View.OnClickListener sendXmlBTnClick = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            String userName = nameET.getText().toString().trim();
            String sex = sexET.getText().toString().trim();
            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(MainActivity.this, "请输入用户名", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            if (TextUtils.isEmpty(sex)) {
                Toast.makeText(MainActivity.this, "请输入性别", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            createXmlFile(userName, sex);
        }
    };

    private void createXmlFile(String userName, String userSex) {
        StringWriter writer = new StringWriter();
        XmlSerializer serializer = Xml.newSerializer();
        try {
            serializer.setOutput(writer);
            serializer.startDocument(null, true);
            serializer.startTag(null, "user");
            serializer.startTag(null, "username");
            serializer.text(userName);
            serializer.endTag(null, "username");
            serializer.startTag(null, "sex");
            serializer.text(userSex);
            serializer.endTag(null, "sex");
            serializer.endTag(null, "user");
            serializer.endDocument();
            serializer.flush();
        } catch (Exception e) {
        }
        String text=writer.toString();

        final SendData data = new SendData(2, text);
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (write != null) {
                    write.println(gson.toJson(data));
                    write.flush();
                    UIhandler.post(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "发送成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }

}
