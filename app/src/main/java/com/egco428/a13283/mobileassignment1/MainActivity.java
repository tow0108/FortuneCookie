package com.egco428.a13283.mobileassignment1;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    private CommentsDataSource dataSource;
    private ArrayAdapter<Comment> courseArrayAdapter;
    public String word,date,position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new CommentsDataSource(this);
        dataSource.open();
        final List<Comment> values = dataSource.getAllComments();
        ListView listView = (ListView) findViewById(R.id.listShow);
        courseArrayAdapter = new CourseArrayAdapter(this, 0,values);
        listView.setAdapter(courseArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,final View view, int position, long l) {
                if (courseArrayAdapter.getCount()>0) {
                    final Comment course = values.get(position);
                    dataSource.deleteComment(course);
                    view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
                        @Override
                        public void run() {

                            courseArrayAdapter.remove(course);
                            view.setAlpha(1);
                        }
                    });
                }
            }
        });

        courseArrayAdapter.notifyDataSetChanged();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //handle presses on the action bar items
        switch (item.getItemId()){
            case R.id.action_add:
                Intent intent = new Intent(this,  ResultActivity.class);
                startActivityForResult(intent, 1);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Comment comment = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == ResultActivity.RESULT_OK){
                String inputData[] = data.getStringArrayExtra(ResultActivity.value);
                    word = inputData[0];
                    date = inputData[1];
                position = inputData[2];
                comment = dataSource.createComment(word,date,position); //add to database
                courseArrayAdapter.add(comment);

            }
        }

    }


    class CourseArrayAdapter extends ArrayAdapter<Comment> {
        Context context;
        List<Comment> objects;
        public CourseArrayAdapter(Context context,int resource,List <Comment> objects) {
            super(context, resource, objects);
            this.context = context;
            this.objects = objects;

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            Comment course = objects.get(position);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);//ประกาศตัวแปร Inflater
            View view = inflater.inflate(R.layout.list_template,null);//ตรง null ถ้าสร้างเป็นกรุ๊บ Layout สามารถใส่เพิ่มได้

            TextView word = (TextView)view.findViewById(R.id.text1);
            word.setText(course.getComment());

            TextView date = (TextView)view.findViewById(R.id.text2);
            date.setText("Date: "+course.getDate());
            if((course.getComment() .equals("Don't Panic"))  || (course.getComment() .equals("Work Harder")) ){
                word.setTextColor(Color.parseColor("#FFA500"));
            }
            else {word.setTextColor(Color.parseColor("#0A29FF"));}



            ImageView image = (ImageView)view.findViewById(R.id.imageView2);
            int res = context.getResources().getIdentifier("image"+course.getPosition(),"drawable",context.getPackageName());//ดึงไฟล์ที่อยู่ใต้โฟลเดอร์ res ผ่าน ID ในประเภท drawable
            image.setImageResource(res);


            return view;
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        dataSource.open();
        ListView listView = (ListView) findViewById(R.id.listShow);
        listView.setAdapter(courseArrayAdapter);

   }
    @Override
    protected void onPause(){
        super.onPause();
        dataSource.close();
    }
}

