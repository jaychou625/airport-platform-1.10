package com.br.service.message;

import com.br.service.constant.MQConstant;
import com.br.service.service.task.handler.XYXDataHandler;
import com.tongtech.tlq.base.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.nio.charset.Charset;

/**
 * XYX 消息接收
 *
 * @Author Zero
 * @Date 2019 02 26
 */
public class XYXReceiver {

    static int MyMsgCount = 0;
    private TlqConnection tlqConnection = null;
    private TlqConnContext tlqConnContext = null;
    private TlqQCU tlqQcu = null;

    @Autowired
    private XYXDataHandler xyxDataHandler;

    public XYXReceiver() {
        try {
            this.tlqConnContext = new TlqConnContext();
            this.tlqConnContext.HostName = "10.24.1.104";
            this.tlqConnContext.ListenPort = 10261;
            // 创建当日目录
            File fileDir = new File("E:\\xyx_xml");
            if(!fileDir.exists()){
                fileDir.mkdirs();
            }
            this.tlqConnContext.FilesDir = fileDir.getPath();
            this.tlqConnection = new TlqConnection(this.tlqConnContext);
            this.tlqQcu = this.tlqConnection.openQCU(MQConstant.QCU_XYX);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread() {
            @Override
            public void run() {
                recvMsg();
            }
        }.start();
    }


    public void recvMsg() {
        int msgCount = 0;
        try {
            while (true) {
                TlqMessage msgInfo = new TlqMessage();
                TlqMsgOpt msgOpt = new TlqMsgOpt();
                msgOpt.QueName = MQConstant.QUEUE_XYX;
                msgOpt.WaitInterval = 1000;
                msgOpt.OperateType = TlqMsgOpt.TLQOT_GET;
                this.tlqQcu.getMessage(msgInfo, msgOpt);
                msgCount = msgCount + 1;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                this.xyxDataHandler.xmlDataParse(new String(msgInfo.getMsgData(), Charset.forName("UTF-8")));
                if (msgOpt.AckMode == TlqMsgOpt.TLQACK_USER) {
                    int ackType = TlqMsgOpt.TLQACK_COMMIT;
                    tlqQcu.ackMessage(msgInfo, msgOpt, ackType);
                }
            }
        } catch (TlqException e) {
            e.printStackTrace();
        } finally {
            MyMsgCount = msgCount;
            try {
                this.tlqQcu.close();
                this.tlqConnection.close();
            } catch (TlqException e) {
                e.printStackTrace();
            }
        }
    }

}
