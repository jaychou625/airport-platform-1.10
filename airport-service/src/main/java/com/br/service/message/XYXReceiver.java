package com.br.service.message;

import com.tongtech.tlq.base.*;

public class XYXReceiver {

    static int MyMsgCount = 0;
    private String myQcuName;
    private String myQueName;
    private int myWaitInterval;
    private int reConnCount;
    private int sleepTime;
    private TlqConnection tlqConnection = null;
    private TlqConnContext tlqConnContext = null;
    private TlqSSLContext tlqSSLContext = null;
    private TlqQCU tlqQcu = null;

    public XYXReceiver(String myQcuName, String myQueName, Integer myWaitInterval) {
        this.myQcuName = myQcuName;
        this.myQueName = myQueName;
        this.myWaitInterval = myWaitInterval;
        try {
            this.tlqConnContext = new TlqConnContext();
            this.tlqConnContext.BrokerId = 111;
            this.tlqConnContext.FilesDir = "E:\\xyx_xml";
            this.tlqConnContext.HostName = "10.24.1.104";
            this.tlqConnContext.ListenPort = 10261;
            this.reConnCount = 200;
            this.sleepTime = 3000;
            this.tlqConnection = new TlqConnection(tlqConnContext);
            this.tlqQcu = tlqConnection.openQCU(this.myQcuName);
        } catch (TlqException e) {
            e.printStackTrace();
        }
        System.out.println(this.myQcuName);
        System.out.println(this.myQueName);
        System.out.println(this.tlqConnContext);
        System.out.println(this.tlqConnection);
        recvMsg();
    }

    public void printMsgInfo(TlqMessage msgInfo) {
        if ((int) msgInfo.MsgType == 1) {
            System.out.println("Received a File Msg");
            System.out.print("msgInfo.MsgId=" + new String(msgInfo.MsgId));
            System.out.println("   msgInfo.MsgSize=" + msgInfo.MsgSize);
        } else {
            System.out.println("Received a Buffer Msg");
            System.out.print("msgInfo.MsgId=" + new String(msgInfo.MsgId));
            System.out.println("   msgInfo.MsgSize=" + msgInfo.MsgSize);
        }
    }

    public void recvMsg() {
        int msgCount = 0;
        try {
            while (true) {
                TlqMessage msgInfo = new TlqMessage();
                TlqMsgOpt msgOpt = new TlqMsgOpt();
                msgOpt.QueName = this.myQueName;
                msgOpt.WaitInterval = this.myWaitInterval;
                /*  msgOpt.MatchOption = TlqMsgOpt.TLQMATCH_PRIORITY; 条件接收
                  msgInfo.Priority = 5;*/
                /* msgOpt.AckMode = TlqMsgOpt.TLQACK_USER;*/
                //用户确认模式
                msgOpt.OperateType = TlqMsgOpt.TLQOT_GET;
                this.tlqQcu.getMessage(msgInfo, msgOpt);
                msgCount = msgCount + 1;
                try {
                    Thread.sleep(2000);                 // 1000 毫秒，也就是1秒.
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                printMsgInfo(msgInfo);
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
        System.out.println("----------GetMsg is over!------------\n");
    }

    public static void main(String[] args) {
        XYXReceiver xyxReceiver = new XYXReceiver("qcu1", "test1", 100);
    }
}


/*    public static void utils(String[] argv) throws Exception {
        String QcuName = "qcu1";
        String QueName = "test";
        int WaitInterval = 100;

        if (argv.length < 1) {
            System.out.println("--------------请输入参数！--------------\n");
            System.out
                    .println("GetMsgCli QcuName QueName WaitInterval");
            return;
        }
        if (argv.length != 3) {
            System.out.println("---------您输入的参数格式不对，请重新输入！---------");
            System.out
                    .println("GetMsgCli QcuName QueName WaitInterval");
        } else {
                   QcuName = argv[0];
                   QueName = argv[1];
        WaitInterval = Integer.parseInt(argv[2]);
        System.out.println(
                "--------------------receive message begin------------------");
        **XYXReceiver GM = new XYXReceiver("qcu1", "test", 100);
        GM.recvMsg();**
        }
        System.out.println("-------共接收消息" + MyMsgCount + "条-------");
    }*/

/*
 tlqConnContext.BrokerId = 111; 根据 tlqcli.conf中的配置
		tlqSSLContext = new TlqSSLContext();
        tlqSSLContext.SSLCAFileName = "E:\\src\\D_TLQ8.1_ClientSSL\\SSL\\secssl\\certs\\cacert.cer";
        tlqSSLContext.SSLCertFileName = "E:\\src\\D_TLQ8.1_ClientSSL\\SSL\\secssl\\certs\\CLIENT.p12";
        tlqSSLContext.SSLCertPwd = "123456";
        tlqSSLContext.SSLFlag = 1;
        tlqSSLContext.SSLProtocolVersion = 1;
        tlqSSLContext.SSLCipher = "AES256-SHA";
		tlqConnection = new TlqConnection(tlqConnContext, tlqSSLContext);
tlqConnection = new TlqConnection(tlqConnContext, reConnCount, sleepTime);*/
