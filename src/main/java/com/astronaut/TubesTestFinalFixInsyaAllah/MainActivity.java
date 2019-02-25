package com.astronaut.TubesTestFinalFixInsyaAllah;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerView list;
    private SimpleAdapter simpleAdapter;
    private Toolbar toolbar;
    private TextView noReminderView;
    private FloatingActionButton addReminderBtn;
    private int temporaryPost;
    private LinkedHashMap<Integer, Integer> IDmap = new LinkedHashMap<>();
    private ReminderDatabase rb;
    private MultiSelector multiSelector = new MultiSelector();
    private AlarmReceiver alarmReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inisialisasi database
        rb = new ReminderDatabase(getApplicationContext());

        // Inisialisasi view
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        addReminderBtn = (FloatingActionButton) findViewById(R.id.add_reminder);
        list = (RecyclerView) findViewById(R.id.reminder_list);
        noReminderView = (TextView) findViewById(R.id.no_reminder_text);

    // cek reminder yg sudah ada
        List<Reminder> mTest = rb.getAllReminders();

        if (mTest.isEmpty()) {
            noReminderView.setVisibility(View.VISIBLE);
        }

        //  recycler view
        list.setLayoutManager(getLayoutManager());
        registerForContextMenu(list);
        simpleAdapter = new SimpleAdapter();
        simpleAdapter.setItemCount(getDefaultItemCount());
        list.setAdapter(simpleAdapter);


        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        // klik FAB
        addReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReminderAddActivity.class);
                startActivity(intent);
            }
        });

        // Inisialisasi alarm
        alarmReceiver = new AlarmReceiver();
    }

    // long press action
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
    }

    // Multi select
    private android.support.v7.view.ActionMode.Callback mDeleteMode = new ModalMultiSelectorCallback(multiSelector) {

        @Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
            getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(android.support.v7.view.ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                // cancel set
                case R.id.discard_reminder:
                    actionMode.finish();
                    for (int i = IDmap.size(); i >= 0; i--) {
                        if (multiSelector.isSelected(i, 0)) {
                            int id = IDmap.get(i);

                            // view dari reminder database using id
                            Reminder temp = rb.getReminder(id);
                            // Delete reminder
                            rb.deleteReminder(temp);
                            // hapus reminder dari tampilan
                            simpleAdapter.removeItemSelected(i);
                            // Delete reminder alarm
                            alarmReceiver.cancelAlarm(getApplicationContext(), id);
                        }
                    }


                    multiSelector.clearSelections();
                    simpleAdapter.onDeleteItem(getDefaultItemCount());
                    Toast.makeText(getApplicationContext(),
                            "terhapus",
                            Toast.LENGTH_SHORT).show();

                // cek saved reminder
                    List<Reminder> mTest = rb.getAllReminders();

                    if (mTest.isEmpty()) {
                        noReminderView.setVisibility(View.VISIBLE);
                    } else {
                        noReminderView.setVisibility(View.GONE);
                    }

                    return true;

                // save btn
                case R.id.save_reminder:
                    actionMode.finish();
                    multiSelector.clearSelections();
                    return true;

                default:
                    break;
            }
            return false;
        }
    };

    // klik item
    private void selectReminder(int mClickID) {
        String mStringClickID = Integer.toString(mClickID);

        Intent i = new Intent(this, ReminderEditActivity.class);
        i.putExtra(ReminderEditActivity.EXTRA_REMINDER_ID, mStringClickID);
        startActivityForResult(i, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        simpleAdapter.setItemCount(getDefaultItemCount());
    }


    // meletakkan yg baru di atas
    @Override
    public void onResume(){
        super.onResume();
        List<Reminder> mTest = rb.getAllReminders();

        if (mTest.isEmpty()) {
            noReminderView.setVisibility(View.VISIBLE);
        } else {
            noReminderView.setVisibility(View.GONE);
        }

        simpleAdapter.setItemCount(getDefaultItemCount());
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    protected int getDefaultItemCount() {
        return 100;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // about
            case R.id.action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // recycler view
    public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.VerticalItemHolder> {
        private ArrayList<ReminderItem> mItems;

        public SimpleAdapter() {
            mItems = new ArrayList<>();
        }

        public void setItemCount(int count) {
            mItems.clear();
            mItems.addAll(generateData(count));
            notifyDataSetChanged();
        }

        public void onDeleteItem(int count) {
            mItems.clear();
            mItems.addAll(generateData(count));
        }

        public void removeItemSelected(int selected) {
            if (mItems.isEmpty()) return;
            mItems.remove(selected);
            notifyItemRemoved(selected);
        }

        @Override
        public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            View root = inflater.inflate(R.layout.recycle_items, container, false);

            return new VerticalItemHolder(root, this);
        }

        @Override
        public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
            ReminderItem item = mItems.get(position);
            itemHolder.setReminderTitle(item.title);
            itemHolder.setReminderDateTime(item.dateTime);
            itemHolder.setReminderRepeatInfo(item.repeat, item.repeatNo, item.repeatType);
            itemHolder.setActiveImage(item.active);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public  class ReminderItem {
            public String title;
            public String dateTime;
            public String repeat;
            public String repeatNo;
            public String repeatType;
            public String active;

            public ReminderItem(String Title, String DateTime, String Repeat, String RepeatNo, String RepeatType, String Active) {
                this.title = Title;
                this.dateTime = DateTime;
                this.repeat = Repeat;
                this.repeatNo = RepeatNo;
                this.repeatType = RepeatType;
                this.active = Active;
            }
        }

       // sorting date time pake compare
        public class DateTimeComparator implements Comparator {
            DateFormat f = new SimpleDateFormat("dd/mm/yyyy hh:mm");

            public int compare(Object a, Object b) {
                String o1 = ((DateTime)a).getDateTime();
                String o2 = ((DateTime)b).getDateTime();

                try {
                    return f.parse(o1).compareTo(f.parse(o2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }


        public  class VerticalItemHolder extends SwappingHolder
                implements View.OnClickListener, View.OnLongClickListener {
            private TextView tv_tittle, tv_dateTime, tv_repeatInfo;
            private ImageView iv_activeImage, iv_thumbnailImg;
            private ColorGenerator colorGenerator = ColorGenerator.DEFAULT;
            private TextDrawable textDrawable;
            private SimpleAdapter simpleAdapter1;

            public VerticalItemHolder(View itemView, SimpleAdapter adapter) {
                super(itemView, multiSelector);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                itemView.setLongClickable(true);


                simpleAdapter1 = adapter;


                tv_tittle = (TextView) itemView.findViewById(R.id.tv_title);
                tv_dateTime = (TextView) itemView.findViewById(R.id.tv_date_time);
                tv_repeatInfo = (TextView) itemView.findViewById(R.id.recycle_repeat_info);
                iv_activeImage = (ImageView) itemView.findViewById(R.id.active_image);
                iv_thumbnailImg = (ImageView) itemView.findViewById(R.id.thumbnail_image);
            }

            // klik reminder item
            @Override
            public void onClick(View v) {
                if (!multiSelector.tapSelection(this)) {
                    temporaryPost = list.getChildAdapterPosition(v);

                    int mReminderClickID = IDmap.get(temporaryPost);
                    selectReminder(mReminderClickID);

                } else if(multiSelector.getSelectedPositions().isEmpty()){
                    simpleAdapter1.setItemCount(getDefaultItemCount());
                }
            }

           // long press
            @Override
            public boolean onLongClick(View v) {
                AppCompatActivity activity = MainActivity.this;
                activity.startSupportActionMode(mDeleteMode);
                multiSelector.setSelected(this, true);
                return true;
            }

            // reminder title
            public void setReminderTitle(String title) {
                tv_tittle.setText(title);
                String letter = "A";

                if(title != null && !title.isEmpty()) {
                    letter = title.substring(0, 1);
                }

                int color = colorGenerator.getRandomColor();

                // Create a circular icon consisting of  a random background colour and first letter of title
                textDrawable = TextDrawable.builder()
                        .buildRound(letter, color);
                iv_thumbnailImg.setImageDrawable(textDrawable);
            }

            // date  time view
            public void setReminderDateTime(String datetime) {
                tv_dateTime.setText(datetime);
            }

            // repeat view
            public void setReminderRepeatInfo(String repeat, String repeatNo, String repeatType) {
                if(repeat.equals("true")){
                    tv_repeatInfo.setText("Tiap " + repeatNo + " " + repeatType);
                }else if (repeat.equals("false")) {
                    tv_repeatInfo.setText("Repeat Off");
                }
            }

            // switch on off
            public void setActiveImage(String active){
                if(active.equals("true")){
                    iv_activeImage.setImageResource(R.drawable.ic_notifications_on_white_24dp);
                }else if (active.equals("false")) {
                    iv_activeImage.setImageResource(R.drawable.ic_notifications_off_grey600_24dp);
                }
            }
        }

        //
        public  ReminderItem generateDummyData() {
            return new ReminderItem("1", "2", "3", "4", "5", "6");
        }


        public List<ReminderItem> generateData(int count) {
            ArrayList<SimpleAdapter.ReminderItem> items = new ArrayList<>();

            // mengambil data reminder
            List<Reminder> reminders = rb.getAllReminders();


            List<String> Titles = new ArrayList<>();
            List<String> Repeats = new ArrayList<>();
            List<String> RepeatNos = new ArrayList<>();
            List<String> RepeatTypes = new ArrayList<>();
            List<String> Actives = new ArrayList<>();
            List<String> DateAndTime = new ArrayList<>();
            List<Integer> IDList= new ArrayList<>();
            List<DateTime> DateTimeSortList = new ArrayList<>();

        // tampil detail
            for (Reminder r : reminders) {
                Titles.add(r.getTitle());
                DateAndTime.add(r.getDate() + " " + r.getTime());
                Repeats.add(r.getRepeat());
                RepeatNos.add(r.getRepeatNo());
                RepeatTypes.add(r.getRepeatType());
                Actives.add(r.getActive());
                IDList.add(r.getID());
            }

            int key = 0;


            for(int k = 0; k<Titles.size(); k++){
                DateTimeSortList.add(new DateTime(key, DateAndTime.get(k)));
                key++;
            }

            Collections.sort(DateTimeSortList, new DateTimeComparator());

            int k = 0;

            // Add data ke recycle view (listitems)
            for (DateTime item:DateTimeSortList) {
                int i = item.getIndex();

                items.add(new SimpleAdapter.ReminderItem(Titles.get(i), DateAndTime.get(i), Repeats.get(i),
                        RepeatNos.get(i), RepeatTypes.get(i), Actives.get(i)));
                IDmap.put(k, IDList.get(i));
                k++;
            }
          return items;
        }
    }
}
