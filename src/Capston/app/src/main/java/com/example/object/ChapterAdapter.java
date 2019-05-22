package com.example.object;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capston.ChapterListActivity;
import com.example.capston.EditChapterActivity;
import com.example.capston.R;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {
    String TAG = "ChapterAdapter";
    // Adapter란 Data 관리를 도와주고 list(RecyclerView)의 갱신을 관리하는 역활을한다.
    private List<Chapter> chapterList;
    private Context context;


    //리스트를 받는 생성자
    public ChapterAdapter(List<Chapter> chapterList, Context context) {
        this.chapterList = chapterList;
        this.context = context;
    }


    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // onCreateViewHolder  viewGroup에서 context를 얻어와서 인플레이트 한다.
        View list = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chapter_item_recycler_view, viewGroup, false);


        return new ChapterViewHolder(list);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterAdapter.ChapterViewHolder chapterViewHolder, int i) {
        // i = position
        // List에서 하나씩 꺼내서 item의 각각의 view에 넣는다.
        Chapter chapter = chapterList.get(i);
        //뷰홀더 참조후 넣어준다.각각의 뷰에 할당.
        Log.w(TAG, "???" + chapter.getChapterNumber() + chapter.getChapterTitle());

        chapterViewHolder.chapterNumberTextView.setText("제" + chapter.getChapterNumber() + "장");
        chapterViewHolder.chapterTitleTextView.setText(chapter.getChapterTitle());
    }

    //recyclerView의 사이즈.
    @Override
    public int getItemCount() {
        return chapterList.size();
    }


    //객체를 보여줄 뷰를 보관하는 내부 클래스.
    class ChapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView chapterNumberTextView;
        private TextView chapterTitleTextView;
        private ImageView chapterEditImagView;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            // itemview => onCreateViewHolder 에서 넘겨준 뷰.
            chapterNumberTextView = itemView.findViewById(R.id.item_chapternumber_textview);
            chapterTitleTextView = itemView.findViewById(R.id.item_chaptertitle_textview);
            chapterEditImagView = itemView.findViewById(R.id.item_chapteredit_Imageview);
            chapterEditImagView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //getAdaterPostion() ==> 몇번쨰 아이템 항목이 재사용된건지 알수있다.
            int postion = getAdapterPosition();

            int id = v.getId();
            switch (id) {
                case R.id.item_chapteredit_Imageview:
                    ChapterListActivity chapterListActivity =(ChapterListActivity) context;
                    String fictionTitle=chapterListActivity.fictionTitle;

                    Intent intent = new Intent(context, EditChapterActivity.class);
                    intent.putExtra("chapterNumber", chapterNumberTextView.getText().toString());
                    intent.putExtra("fictionTitle", fictionTitle);
                    context.startActivity(intent);
                    break;

            }

        }
    }
}