package hlistview.demo;

import hlistview.demo.adapter.MyAdapter;
import hlistview.demo.view.HListView;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.myhorizationlistview.R;
import android.app.Activity;
import android.os.Bundle;



public class MainActivity extends Activity {
	private HListView listView ; 
	private MyAdapter mAdapter ;
	
	private List<Map<String, Object>> list ;
	private Map<String,Object> map ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		
	}
	
	private void initView() {
		listView = (HListView) findViewById(R.id.listView);
		
		list = this.getData();
		
		mAdapter = new MyAdapter(MainActivity.this, list);
		listView.setAdapter(mAdapter);
	}

	private List<Map<String, Object>> getData(){
		
		int [] pic = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,
				R.drawable.f,R.drawable.g,R.drawable.h,R.drawable.i,R.drawable.j};
		
		list = new ArrayList<Map<String,Object>>();
		
		for(int i = 0;i<pic.length;i++){
			map =new HashMap<String, Object>();
			map.put("index", "╣з"+(i+1)+"уе");
			map.put("img", pic[i]);
			list.add(map);
		}
		return list;
		
	}
}
