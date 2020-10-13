package tisserand.alexey.justquiz.adapters;

import android.content.Context;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;

public class RecordAdapter
{
    private static String FILE_RECORDS = "memoria-records";

    private TreeMap<Integer, String> recMap;
    private Context mContext;
    private int magnifier = 100;

    @SuppressWarnings("unchecked")
    public RecordAdapter(Context context)
    {
        recMap = new TreeMap<Integer, String>();
        mContext = context;


        try {
            FileInputStream fis = mContext.openFileInput(FILE_RECORDS);
            ObjectInputStream is = new ObjectInputStream(fis);
            recMap = (TreeMap<Integer, String>) is.readObject();
            is.close();
        } catch (Exception e) {
            Toast.makeText(mContext, "Произошла ошибка чтения таблицы рекордов", Toast.LENGTH_LONG).show();
        }
    }


    public void writeRecords()
    {
        try {
            FileOutputStream fos = mContext.openFileOutput(FILE_RECORDS, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(recMap);
            os.close();
        } catch (Exception e) {
            Toast.makeText(mContext, "Произошла ошибка записи в таблицу рекордов", Toast.LENGTH_LONG).show();
        }
    }

    public void clearRecord(){
        recMap.clear();
        writeRecords();
    }

    public void addRecord(String name, Integer num)
    {
        int countRepetition = 0;
        Set<Integer> setKey = recMap.keySet();
        for(Integer k: setKey){
            if(k / magnifier == num){
                countRepetition++;
            }
        }

        recMap.put(num * magnifier + countRepetition, name);
        for(int i = 5; i < recMap.size(); i++){
            recMap.remove(minElement());
        }
    }
    private int minElement(){
        return recMap.firstKey();
    }

    public ArrayList<String> getArrayRecord()
    {
        int n = recMap.size();
        ArrayList<String> arrayRecord = new ArrayList<String>();
        for(Integer k: recMap.keySet()){
            arrayRecord.add(String.valueOf(n) + "  —  "+ recMap.get(k) + "  —  " + k / magnifier);
            n--;
        }
        Collections.reverse(arrayRecord);
        return arrayRecord;
    }
}
