package com.okr.family;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OKRAdapter okrAdapter;
    private List<OKRItem> okrList;
    private Spinner periodSpinner;
    private EditText searchEditText;
    private Button addOkrButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupListeners();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        periodSpinner = findViewById(R.id.periodSpinner);
        searchEditText = findViewById(R.id.searchEditText);
        addOkrButton = findViewById(R.id.addOkrButton);

        // 设置周期选择器
        String[] periods = {"周", "月", "年"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_dropdown_item, periods);
        periodSpinner.setAdapter(adapter);
    }

    private void setupRecyclerView() {
        okrList = new ArrayList<>();
        okrAdapter = new OKRAdapter(this, okrList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(okrAdapter);

        // 添加示例数据
        addSampleData();
    }

    private void addSampleData() {
        okrList.add(new OKRItem("提升家庭生活质量", "改善居住环境，增进家庭和谐", 
            Arrays.asList("每周大扫除一次", "每月家庭聚餐两次", "每天共同散步"), 
            "张三", "周", new Date(), Arrays.asList("@李四 协助打扫")));
        okrList.add(new OKRItem("提高个人技能", "掌握新技能，提升自我", 
            Arrays.asList("每周学习新技术", "每月完成一个项目", "每天阅读30分钟"), 
            "李四", "月", new Date(), Arrays.asList("@王五 讨论技术问题")));
        okrAdapter.notifyDataSetChanged();
    }

    private void setupListeners() {
        addOkrButton.setOnClickListener(v -> showAddOKRDialog());

        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterByPeriod(((String) parent.getItemAtPosition(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void showAddOKRDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_okr, null);
        builder.setView(dialogView);

        EditText objectiveInput = dialogView.findViewById(R.id.objectiveInput);
        EditText descriptionInput = dialogView.findViewById(R.id.descriptionInput);
        EditText keyResultsInput = dialogView.findViewById(R.id.keyResultsInput);
        EditText assigneeInput = dialogView.findViewById(R.id.assigneeInput);
        Spinner periodInput = dialogView.findViewById(R.id.periodInput);
        EditText mentionsInput = dialogView.findViewById(R.id.mentionsInput);

        // 设置周期选择器
        String[] periods = {"周", "月", "年"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_dropdown_item, periods);
        periodInput.setAdapter(adapter);

        builder.setTitle("添加新的OKR")
               .setPositiveButton("保存", (dialog, which) -> {
                   String objective = objectiveInput.getText().toString();
                   String description = descriptionInput.getText().toString();
                   String keyResultsText = keyResultsInput.getText().toString();
                   String assignee = assigneeInput.getText().toString();
                   String period = (String) periodInput.getSelectedItem();
                   String mentionsText = mentionsInput.getText().toString();

                   if (!objective.isEmpty()) {
                       List<String> keyResults = Arrays.asList(keyResultsText.split("\\n"));
                       List<String> mentions = Arrays.asList(mentionsText.split("\\n"));
                       OKRItem newItem = new OKRItem(objective, description, keyResults, 
                           assignee, period, new Date(), mentions);
                       okrList.add(newItem);
                       okrAdapter.notifyDataSetChanged();
                   }
               })
               .setNegativeButton("取消", null)
               .show();
    }

    private void filterByPeriod(String period) {
        List<OKRItem> filteredList = new ArrayList<>();
        for (OKRItem item : okrList) {
            if (item.getPeriod().equals(period)) {
                filteredList.add(item);
            }
        }
        okrAdapter.updateList(filteredList);
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            okrAdapter.updateList(okrList);
            return;
        }

        List<OKRItem> filteredList = new ArrayList<>();
        for (OKRItem item : okrList) {
            if (item.getObjective().toLowerCase().contains(query.toLowerCase()) ||
                item.getDescription().toLowerCase().contains(query.toLowerCase()) ||
                item.getAssignee().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        okrAdapter.updateList(filteredList);
    }
}

// 数据模型类
class OKRItem {
    private String objective;
    private String description;
    private List<String> keyResults;
    private String assignee;
    private String period;
    private Date createdDate;
    private List<String> mentions;

    public OKRItem(String objective, String description, List<String> keyResults, 
                   String assignee, String period, Date createdDate, List<String> mentions) {
        this.objective = objective;
        this.description = description;
        this.keyResults = keyResults;
        this.assignee = assignee;
        this.period = period;
        this.createdDate = createdDate;
        this.mentions = mentions;
    }

    // Getters
    public String getObjective() { return objective; }
    public String getDescription() { return description; }
    public List<String> getKeyResults() { return keyResults; }
    public String getAssignee() { return assignee; }
    public String getPeriod() { return period; }
    public Date getCreatedDate() { return createdDate; }
    public List<String> getMentions() { return mentions; }
}

// RecyclerView适配器
class OKRAdapter extends RecyclerView.Adapter<OKRAdapter.ViewHolder> {
    private Context context;
    private List<OKRItem> okrList;

    public OKRAdapter(Context context, List<OKRItem> okrList) {
        this.context = context;
        this.okrList = okrList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_okr, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OKRItem item = okrList.get(position);
        
        holder.objectiveTextView.setText(item.getObjective());
        holder.descriptionTextView.setText(item.getDescription());
        holder.assigneeTextView.setText("负责人: " + item.getAssignee());
        holder.periodTextView.setText("周期: " + item.getPeriod());
        
        StringBuilder krBuilder = new StringBuilder();
        for (int i = 0; i < item.getKeyResults().size(); i++) {
            krBuilder.append((i + 1)).append(". ").append(item.getKeyResults().get(i));
            if (i < item.getKeyResults().size() - 1) {
                krBuilder.append("\n");
            }
        }
        holder.keyResultsTextView.setText(krBuilder.toString());
        
        StringBuilder mentionsBuilder = new StringBuilder();
        for (int i = 0; i < item.getMentions().size(); i++) {
            mentionsBuilder.append(item.getMentions().get(i));
            if (i < item.getMentions().size() - 1) {
                mentionsBuilder.append("\n");
            }
        }
        holder.mentionsTextView.setText(mentionsBuilder.toString());
    }

    @Override
    public int getItemCount() {
        return okrList.size();
    }

    public void updateList(List<OKRItem> newList) {
        this.okrList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView objectiveTextView;
        TextView descriptionTextView;
        TextView assigneeTextView;
        TextView periodTextView;
        TextView keyResultsTextView;
        TextView mentionsTextView;

        ViewHolder(View itemView) {
            super(itemView);
            objectiveTextView = itemView.findViewById(R.id.objectiveTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            assigneeTextView = itemView.findViewById(R.id.assigneeTextView);
            periodTextView = itemView.findViewById(R.id.periodTextView);
            keyResultsTextView = itemView.findViewById(R.id.keyResultsTextView);
            mentionsTextView = itemView.findViewById(R.id.mentionsTextView);
        }
    }
}
