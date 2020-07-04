package udirect.com.yoshow;
public class publicvar
{
        private static publicvar instance;


        private int data;


        private publicvar(){}

        public void setData(int d){
            this.data=d;
        }
        public int getData(){
            return this.data;
        }

        public static synchronized publicvar getInstance(){
            if(instance==null){
                instance=new publicvar();
            }
            return instance;
        }
    }
