package notification;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smpp.Connection;
import org.smpp.Session;
import org.smpp.TCPIPConnection;
import org.smpp.pdu.BindResponse;
import org.smpp.pdu.BindTransmitter;
import org.smpp.pdu.SubmitSM;

import java.util.Properties;

public class SmsService {

    private final String systemId;
    private final String password;
    private final String systemType;
    private final String sourceAddress;
    private final String host;
    private final int port;

    private static final Logger logger = LogManager.getLogger(SmsService.class);

    public SmsService(){
        Properties config = loadConfig();
        systemId = config.getProperty("system_id");
        password = config.getProperty("password");
        systemType= config.getProperty("system_type");
        sourceAddress = config.getProperty("source_addr");
        host = config.getProperty("host");
        port = Integer.parseInt(config.getProperty("port"));
    }

    private Properties loadConfig() {
        try {
            Properties props = new Properties();
            props.load(SmsService.class.getClassLoader()
                    .getResourceAsStream("sms.properties"));
            return props;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load sms configuration", e);
        }
    }

    public void sendCode(String destination, String code) {
        Connection connection;
        Session session;

        try {
            connection = new TCPIPConnection(host, port);
            session = new Session(connection);

            BindTransmitter bindRequest = new BindTransmitter();
            bindRequest.setSystemId(systemId);
            bindRequest.setPassword(password);
            bindRequest.setSystemType(systemType);
            bindRequest.setInterfaceVersion((byte) 0x34);
            bindRequest.setAddressRange(sourceAddress);

            BindResponse bindResponse = session.bind(bindRequest);
            if (bindResponse.getCommandStatus() != 0) {
                logger.error(STR."Ошибка отправки SMS по номеру \{destination} ");
            }

            SubmitSM submitSM = new SubmitSM();
            submitSM.setSourceAddr(sourceAddress);
            submitSM.setDestAddr(destination);
            submitSM.setShortMessage("Ваш код: " + code);

            session.submit(submitSM);

        } catch (Exception e) {
            logger.error(STR."Ошибка отправки SMS по номеру \{destination}" + e.getMessage());
        }
    }
}
