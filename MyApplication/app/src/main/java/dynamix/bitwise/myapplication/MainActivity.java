package dynamix.bitwise.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;

    private int i;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adds cards onto the View



        arrayList = new ArrayList<>();
        arrayList.add("Lenny");
        arrayList.add("Ricardo");
        arrayList.add("Glenn");
        arrayList.add("Zena");
        arrayList.add("Lady");
        arrayList.add("Benny");
        arrayList.add("Frank");
        arrayList.add("Juice");
        arrayList.add("Dog");
        arrayList.add("Carl");

        /*picList = new ArrayList<>();
        picList.add("@drawable/img1");
        picList.add("@drawable/img2");
        picList.add("@drawable/img3");
        picList.add("@drawable/img4");
        picList.add("@drawable/img5");
        picList.add("@drawable/img6");
        picList.add("@drawable/img7");
        picList.add("@drawable/img8");
        picList.add("@drawable/img9");
        picList.add("@drawable/img10");*/

        arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.helloText, arrayList );
        //picAdapter = new ArrayAdapter<String>(this, R.layout.item, R.id.imgView, picList);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        //flingContain.setAdapter(picAdapter);  //This is where your problem stems from
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                arrayList.remove(0);
                //picList.remove(0);
               // picAdapter.notifyDataSetChanged();
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(MainActivity.this, "Left!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(MainActivity.this, "Right!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                arrayList.add("XML ".concat(String.valueOf(i)));
               // picList.add("@drawable/puppy");
                arrayAdapter.notifyDataSetChanged();
               // picAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionaly add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Click!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}

