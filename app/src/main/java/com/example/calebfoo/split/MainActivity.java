package com.example.calebfoo.split;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;




import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Hashtable foodTable = new Hashtable<String,Double>();
    Double foodTotalPrice;
    Hashtable nameList = new Hashtable<String,String>();
    List<Boolean> checked = new ArrayList<>();
    int s = 0;
    String allPersons="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void addFood(View view) {

        LinearLayout linear = findViewById(R.id.LineatLayout);
        EditText foodname = findViewById(R.id.foodName);
        EditText foodprice = findViewById(R.id.foodPrice);
        foodTable.put(foodname.getText().toString(),Double.parseDouble(foodprice.getText().toString()));
        String newText = foodname.getText().toString()+" "+ foodprice.getText().toString();

        TextView tv1 = new TextView(this);
        tv1.setText(newText);

        linear.addView(tv1);
        foodname.setText("");
        foodprice.setText("");

    }
    public void onConfirm(View view){
        setContentView(R.layout.check_main);

        LinearLayout newLinear = this.findViewById(R.id.dropView);
        double total = 0;
        Set<String> keys = foodTable.keySet();
        for(String key:keys){
            total= total + Double.parseDouble(foodTable.get(key).toString());
            String newText = key + " "+ foodTable.get(key).toString();
            TextView tv1 = new TextView(this);
            tv1.setText(newText);
            newLinear.addView(tv1);
        }
        TextView totalView = findViewById(R.id.TotalView);
        foodTotalPrice=total;
        totalView.setText("TOTAL: $ " + String.valueOf(total));
    }
    public void onCorrect(View view){
        LinearLayout popup = this.findViewById(R.id.popUP);
        popup.setVisibility(View.VISIBLE);
    }
    public void onSplit(View view) {
        setContentView(R.layout.splitterlayout);
        final LinearLayout split = findViewById(R.id.splitLayout);
        split.setVisibility(View.VISIBLE);
        final TextView splitText = findViewById(R.id.SplitTextView);
        Spinner spinner = findViewById(R.id.spinner2);
        spinner.setVisibility(View.VISIBLE);
        List<String> list = new ArrayList<>();
        for (int i = 0; i <= 30; i++) {

            list.add(String.valueOf(i));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                String text1 = "Each person has to pay $" + String.valueOf(Double.valueOf(foodTotalPrice / Integer.parseInt(item.toString())));
                splitText.setText(text1);
                splitText.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }
    public void onADVSeperate(View view){
        setContentView(R.layout.seperatelayout);
    }
    public void onSeperate(View view){
        LinearLayout linear = findViewById(R.id.nameList);
        EditText nameadd = findViewById(R.id.nameAdder);
        nameList.put(nameadd.getText().toString(),"");
        TextView tv1 = new TextView(this);
        tv1.setText(nameadd.getText().toString());
        linear.addView(tv1);
        nameadd.setText("");

    }
    public void addButtons(List<String> nameLister,LinearLayout foodButton ,TextView SetText){
        SetText.setText(nameLister.get(s));
        ViewGroup layout = findViewById(R.id.foodBtns);
        for (int i = 0; i < foodButton.getChildCount(); i++) {

            checked.set(i, false);
            View child = layout.getChildAt(i);
            if (child instanceof ToggleButton) {
                ToggleButton btn2 = (ToggleButton) child;
                btn2.setChecked(false);

            }


        }
    }
    public void onDone(View view) {
        LinearLayout nameLayout = this.findViewById(R.id.nameLayout);
        nameLayout.setVisibility(View.INVISIBLE);
        final LinearLayout chooseLayout = this.findViewById(R.id.chooseLayout);
        chooseLayout.setVisibility(View.VISIBLE);
        final LinearLayout foodButtons = findViewById(R.id.foodBtns);
        final TextView nameChosen = findViewById(R.id.selectedName);

        final List<String> nameListed = Collections.list(nameList.keys());
        final List<String> foodies = Collections.list(foodTable.keys());

        final List<Integer> foodChosenchoices= new ArrayList<Integer>();
        for(int i=0;i<nameListed.size();i++)
        {
            foodChosenchoices.add(0);
        }
        nameChosen.setText(nameListed.get(s));
        for(int i=0; i<foodies.size();i++)
        {
            if(s<1)
            {
                checked.add(false);
            }
            else {
                checked.set(i, false);
            }
            final ToggleButton btn1 = new ToggleButton(MainActivity.this);
            btn1.setId(i+1);
            btn1.setText(foodies.get(i));
            btn1.setTextOff(foodies.get(i));
            btn1.setTag(i);
            btn1.setTextOn(foodies.get(i));
            foodButtons.addView(btn1);
            btn1.setOnClickListener(new AdapterView.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(btn1.isChecked()) {
                        checked.set((Integer.parseInt(v.getTag().toString())), true);
                    }else if(!btn1.isChecked()){
                        checked.set((Integer.parseInt(v.getTag().toString())), false);
                    }
                }
            });

        }
        final Button doneBtn= findViewById(R.id.cmpleteBtn);

        doneBtn.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected="";
                for(int i=0;i<checked.size();i++){
                    if(checked.get(i))
                    {
                        selected = selected + String.valueOf(i) + " ";
                        addPersonFoodCount(foodChosenchoices,i);
                    }
                }
                nameList.put(nameListed.get(s),selected);
                Toast.makeText(getApplicationContext(),selected, Toast.LENGTH_SHORT).show();
                s+=1;
                if(s<nameListed.size()) {
                    addButtons(nameListed,foodButtons,nameChosen);
                }
                if((nameListed.size()) == s) {
                    setContentView(R.layout.final_layoutmain);
                    TextView finalView = findViewById(R.id.finalView);
                    TextView totalview = findViewById(R.id.totalView2);
                    finalView.setTextSize(14f);
                    Double price;
                    List<String> selectedList = new ArrayList<>(nameList.values());
                    List<Double> totalPriceList = new ArrayList<>();
                    int selectLength =selectedList.size();
                    for (int i = 0; i < selectedList.size(); i++) {
                        price = 0.0;
                        String selector = selectedList.get(i);
                        String[] selectorList = selector.split(" ");
                        for(int b=0;b<selectLength;b++){
                            if (b==selectorList.length) break;
                            List<Double> priceList = new ArrayList<>(foodTable.values());
                            price = price + priceList.get(Integer.parseInt(selectorList[b]))/foodChosenchoices.get(b);
                        }
                        totalPriceList.add(price);
                        allPersons = nameListed.get(i) + " " + String.valueOf(price) + "\n" + allPersons;

                    }
                    finalView.setText(allPersons);
                    totalview.setText(String.valueOf(foodTotalPrice));
                }

            }

        });

    }
    public void addPersonFoodCount(List<Integer> counter, int count){
        int b = counter.get(count);
        counter.set(count,b+1);
    }


}


