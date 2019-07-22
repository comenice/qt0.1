package concurrent;

public class test {


    private static boolean ready ;
    private static int number ;

    private static class MyThread extends Thread {

        @Override
        public void run() {
           // System.out.println( "run" );
            while ( !ready ){
                System.out.println( "run" );

                System.out.println("number  : " +  number );
            }
        }
    }

    public static void main(String[] args) {
           Thread t1 = new MyThread();
           t1.setPriority( 10 );
           t1.start();

           number = 100;
           ready = true ;

    }



}
