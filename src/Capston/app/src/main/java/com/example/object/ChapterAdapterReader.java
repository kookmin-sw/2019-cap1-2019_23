package com.example.object;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.capston.R;
import com.example.capston.ReadChapterActivity;

import java.util.List;

public class ChapterAdapterReader extends RecyclerView.Adapter<ChapterAdapterReader.ChapterViewHolderReader>{

    private List<Chapter> chapterList;
    private Context context;


    //리스트를 받는 생성자
    public ChapterAdapterReader(List<Chapter> chapterList, Context context) {
        this.chapterList = chapterList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChapterViewHolderReader onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View list = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chapter_read_item_recycler_view, viewGroup, false);
        return new ChapterViewHolderReader(list);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolderReader chapterViewHolderReader, int i) {
        // i = position
        // List에서 하나씩 꺼내서 item의 각각의 view에 넣는다.
        Chapter chapter = chapterList.get(i);
        // 바인딩.
        chapterViewHolderReader.chapterNumberTextView.setText("제"+chapter.getChapterNumber()+"장");
        chapterViewHolderReader.chapterTitleTextView.setText(chapter.getChapterTitle());
        chapterViewHolderReader.authorAccount = chapter.getAuthorAccount();
        chapterViewHolderReader.fictionTitle = chapter.getFictionTitle();

    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    class ChapterViewHolderReader extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView chapterNumberTextView;
        private TextView chapterTitleTextView;
        private ImageView chapterReadImagView;

        private String authorAccount;
        private String fictionTitle;


        public ChapterViewHolderReader(@NonNull View itemView) {
            super(itemView);
            chapterNumberTextView = itemView.findViewById(R.id.item_chapter_read_number_textview);
            chapterTitleTextView = itemView.findViewById(R.id.item_chapter_read_title_textview);
            chapterReadImagView = itemView.findViewById(R.id.item_chapter_read_edit_Imageview);
            chapterReadImagView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            Intent intent = new Intent(context, ReadChapterActivity.class);

            intent.putExtra("authorAccount",authorAccount);
            intent.putExtra("fictionTitle",fictionTitle);
            intent.putExtra("chapterNumber",chapterNumberTextView.getText().toString());
            intent.putExtra("chapterTitle",chapterTitleTextView.getText().toString());
            context.startActivity(intent);
        }

    }
}
