package i5b5.daniel.serszen.pz.controller;

import i5b5.daniel.serszen.pz.model.exceptions.InvalidXmlFormatException;
import i5b5.daniel.serszen.pz.model.mybatis.dto.Car;
import i5b5.daniel.serszen.pz.model.mybatis.dto.CarPart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;

public class XmlParser {
    private static final Logger logger;
    private static JAXBContext carPartContext;
    private static JAXBContext carContext;

    static {
        logger = LogManager.getLogger(XmlParser.class);
        try {
            carPartContext = JAXBContext.newInstance(CarPart.class);
            carContext = JAXBContext.newInstance(Car.class);
        } catch (JAXBException e) {
            logger.error(e);
        }
    }

    public static CarPart unmarshallCarPartXml(File file) throws InvalidXmlFormatException {
        try {
            return (CarPart) carPartContext.createUnmarshaller().unmarshal(file);
        } catch (JAXBException e) {
            logger.error(e);
            throw new InvalidXmlFormatException("Zły format pliku", e);
        }
    }

    public static Car unmarshallCarXml(File file) throws InvalidXmlFormatException {
        try {
            return (Car) carContext.createUnmarshaller().unmarshal(file);
        } catch (JAXBException e) {
            logger.error(e);
            throw new InvalidXmlFormatException("Zły format pliku", e);
        }
    }
}