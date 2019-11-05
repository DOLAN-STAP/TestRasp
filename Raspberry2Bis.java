package raspberry2bis;
import java.io.File;
import java.io.IOException;
import org.eclipse.paho.client.mqttv3.*;

public class Raspberry2Bis 
{
    public static MqttClient client;
    public static MqttMessage message;
    public static void main(String[] args) throws MqttException, InterruptedException {
       client = new MqttClient("tcp://RASPBY:1883", "bouttonA");     

       client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {}
            
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception
            {
                if("BouttonA".equals(topic))
                {
                    if(message.toString().equals("Appuyer"))
                    {
                        File b = new File("/tmp/oc/write/B");
                        try 
                        {
                            b.createNewFile();
                        }
                        catch (IOException ex)
                        {}
                    }
                    else
                    {
                        File b = new File("/tmp/oc/write/B");
                        b.delete();
                    }
                }
            }
            
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });
        
       client.connect();
       client.subscribe("bouttonA");
       message = new MqttMessage();
       int i = 0;
       while(i < 99999)
       {
           File f = new File("/tmp/oc/read/A");
           if(f.exists())
           {
               message.setPayload("Appuye".getBytes());
           }
           else
           {
               message.setPayload("NonAppuye".getBytes());
           }
           client.publish("BouttonA", message);
           Thread.sleep(100);
       }

       client.disconnect();
    }
}
