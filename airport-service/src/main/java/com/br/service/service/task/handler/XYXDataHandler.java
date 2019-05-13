package com.br.service.service.task.handler;

import com.br.entity.task.FlightInfo;
import com.br.mapper.FlightInfoMapper;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.Resource;
import java.io.File;

/**
 * 新运行数据处理类
 *
 * @Author Zero
 * @Date 2019 05 09
 */
public class XYXDataHandler {

    // 航班信息Mapper
    @Resource
    private FlightInfoMapper flightInfoMapper;


    // XYXXml观察者
    @Autowired
    private XYXXmlVisitor xyxXmlVisitor;

    /**
     * XML文件数据解释
     *
     * @param fileName XML文件名称
     * @return boolean
     */
    public boolean xmlDataParse(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return false;
        }
        SAXReader saxReader = new SAXReader();
        try {
            Document doc = saxReader.read(new File(fileName));
            Element root = doc.getRootElement();
            root.accept(this.xyxXmlVisitor);
            FlightInfo flightInfo = XYXXmlVisitor.flightInfo;
            if(flightInfo != null){
                this.flightInfoMapper.add(flightInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
