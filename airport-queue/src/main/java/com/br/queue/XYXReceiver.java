package com.br.queue;

import com.br.constant.MQConstant;
import com.br.service.task.handler.XYXDataHandler;
import com.tongtech.tlq.base.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.nio.charset.Charset;

public class XYXReceiver {

    private static int MyMsgCount = 0;
    private TlqConnection tlqConnection = null;
    private TlqConnContext tlqConnContext = null;
    private TlqQCU tlqQcu = null;
    private Thread xyxThread;

    /*--------------XYX数据处理器------------------*/
    @Autowired
    private XYXDataHandler xyxDataHandler;

    public XYXReceiver() {
        try {
            this.tlqConnContext = new TlqConnContext();
            this.tlqConnContext.HostName = "10.24.1.104";
            this.tlqConnContext.ListenPort = 10261;
            /*--------------如果存放的目录不存在 创建目录------------------*/
            File fileDir = new File("E:\\xyx_xml");
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            this.tlqConnContext.FilesDir = fileDir.getPath();
            this.tlqConnection = new TlqConnection(this.tlqConnContext);
            this.tlqQcu = this.tlqConnection.openQCU(MQConstant.QCU_XYX);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*--------------创建接收数据线程------------------*/
        if (this.xyxThread == null) {
            this.xyxThread = new Thread() {
                @Override
                public void run() {
                    recvMsg();
                }
            };
        }
        /*--------------判断接收数据线程是否活着------------------*/
        if (!this.xyxThread.isAlive()) {
            this.xyxThread.start();
        }
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

