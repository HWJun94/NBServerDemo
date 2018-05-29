//import org.apache.http.Header;
//import org.apache.http.HeaderElement;
//
//public class MessageStorageTest {
//
//    public static void main(String[] args) {
////        Thread thread = new Thread(new Runnable() {
////            @Override
////            public void run() {
////                for(int i = 0; i < 20; i++) {
////                    String message = String.valueOf(i);
////                    QueueMessageProducer queueMessageProducer = new QueueMessageProducer(message);
////                    MessageStorage.executeProducer(queueMessageProducer);
////                    try {
////                        Thread.sleep(new Random().nextInt(1000));
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
////                }
////            }
////        });
////        thread.start();
////        for (int j = 0; j < 20; j++) {
////            QueueMessageConsumer queueMessageConsumer = new QueueMessageConsumer();
////            MessageStorage.executeConsumer(queueMessageConsumer);
////            try {
////                Thread.sleep(new Random().nextInt(1000));
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////        }
//    }
//}
