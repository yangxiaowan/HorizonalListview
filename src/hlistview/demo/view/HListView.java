package hlistview.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;

/**
 * ��ӹ���,ÿ����ָ������ʱ�����һ��item�ľ��� 1)������Ļ��ʾ���̶���view 2) ���Ź�������������view
 * 
 * @author csdn chunqiuwei
 *
 */
public class HListView extends ViewGroup {
	/** �洢�����õ�Adapter **/
	private ListAdapter listAdapter;

	private int rightIndex = 0;
	private int leftIndex = -1;
	private int distanceX = 0;
	private GestureDetector gestureDetector;
	private int leftOffset = 0;

	public HListView(Context context) {
		super(context);
		initParams();
	}

	public HListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initParams();
	}

	public HListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initParams();
	}

	private void initParams() {
		gestureDetector = new GestureDetector(getContext(), grestureListener);
	}

	private OnGestureListener grestureListener = new GestureDetector.SimpleOnGestureListener() {
		/**
		 * ������ָ ��1��MotionEvent ACTION_DOWN, ���ACTION_MOVE����
		 *
		 * @param e1
		 *            ���������ƶ��¼�
		 * @param e2
		 *            ��ǰ���Ƶ���ƶ��¼�
		 * @param distanceX
		 *            �����ϴε���onScroll������ʱ��x������ľ���
		 * @param distanceY
		 *            �����ϴε���onScroll ������ʱ��y������ľ���
		 */
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			Log.e("", "distanceX=="+distanceX);
			HListView.this.distanceX= (int) distanceX;
			// ��Ȼ��Ҫ�ػ�
			requestLayout();
			return true;

		};
	};

	// @Override
	public ListAdapter getAdapter() {
		return listAdapter;
	}

	// @Override
	public void setAdapter(ListAdapter adapter) {
		this.listAdapter = adapter;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return gestureDetector.onTouchEvent(event) || true;
	}

	/**
	 * ����ÿ��child�Ŀ�͸�
	 * 
	 * @param view
	 * @return
	 */
	private View measureChild(View view) {
		LayoutParams params = view.getLayoutParams();
		if (params == null) {
			params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			view.setLayoutParams(params);
		}

		view.measure(
				MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.AT_MOST),
				MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.AT_MOST));
		return view;

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {

		if (listAdapter == null) {
			return;
		}
		removeAnvisiableViews(-distanceX);
		addRightChildViews(-distanceX);
		addLeftChildViews(-distanceX);
		layoutChildViews(-distanceX);

	}

	/***
	 * ɾ����������view
	 */
	private void removeAnvisiableViews(int distanceX) {
		// �Ƴ���߿�������view
		View firtVisiableView = getChildAt(0);
		if (firtVisiableView != null
				&& distanceX + firtVisiableView.getRight() <= 0) {
			removeViewInLayout(firtVisiableView);
			leftOffset += firtVisiableView.getMeasuredWidth();
			leftIndex++;
		}

		// �Ƴ��ұ߿�������view
		View lastVisialbeView = getChildAt(getChildCount() - 1);
		if (lastVisialbeView != null
				&& lastVisialbeView.getLeft() + distanceX >= getWidth()) {
			removeViewInLayout(lastVisialbeView);
			rightIndex--;
		}

		
	}

	private void addRightChildViews(int distanceX) {
		// 2.����Ļ�����ܵ���ʾItem��ע��տ�ʼ��ʱ����û��
		View rightChildView = getChildAt(getChildCount() - 1);
		// ��ȡ��childView�ұ߿����parentView��߿�ľ���
		int rightEdge = rightChildView != null ? rightChildView.getRight() : 0;
		while (rightEdge + distanceX < getWidth()
				&& rightIndex < listAdapter.getCount()) {
			Log.e("", "rightIndex== "+rightIndex);
			View child = listAdapter.getView(rightIndex, null, null);
			child = measureChild(child);
			addViewInLayout(child, -1, child.getLayoutParams(), true);
			rightEdge += child.getMeasuredWidth();
			rightIndex++;
		}
	}

	private void addLeftChildViews(int distanceX) {
		View leftChildView = getChildAt(0);
		int leftEdge = leftChildView != null ? leftChildView.getLeft() : 0;
		while (leftEdge + distanceX > 0 && leftIndex >= 0) {
			Log.e("", "leftIndex== "+leftIndex);

			View child = listAdapter.getView(leftIndex, null, null);
			child = measureChild(child);
			addViewInLayout(child, 0, child.getLayoutParams(), true);
			leftEdge -= child.getMeasuredWidth();
			leftIndex--;
			leftOffset -= child.getMeasuredWidth();
		}
	}

	/**
	 * 3.�Ѳ���2��ӵ�viewͨ��Layout���ֵ�parentView��
	 */
	private void layoutChildViews(int distanceX) {
		if(getChildCount()==0) {
			return;
		}
		leftOffset += distanceX;
		int childLeft = leftOffset;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			int childWidth = child.getMeasuredWidth();
			child.layout(childLeft, 0, childWidth + childLeft,
					child.getMeasuredHeight());
			// ������õ�д����
			childLeft += childWidth + child.getPaddingRight();
		}
		
	}
}
