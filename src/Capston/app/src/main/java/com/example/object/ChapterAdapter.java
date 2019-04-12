package com.example.object;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.capston.R;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    // Adapter란 Data 관리를 도와주고 list(RecyclerView)의 갱신을 관리하는 역활을한다.
    private List<Chapter> chapterList;

    //리스트를 받는 생성자
    public ChapterAdapter(List<Chapter> chapterList) {
        this.chapterList = chapterList;
    }



    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // onCreateViewHolder  viewGroup에서 context를 얻어와서 인플레이트 한다.
        View list = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fiction_item_recycler_view, viewGroup, false);
        return new ChapterViewHolder(list);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterAdapter.ChapterViewHolder chapterViewHolder, int i) {
        // i = position
        // List에서 하나씩 꺼내서 item의 각각의 view에 넣는다.
        Chapter chapter =chapterList.get(i);
        //뷰홀더 참조후 넣어준다.각각의 뷰에 할당.
    }

    //recyclerView의 사이즈.
    @Override
    public int getItemCount() {
        return chapterList.size();
    }



    //객체를 보여줄 뷰를 보관하는 내부 클래스.
    static class ChapterViewHolder extends RecyclerView.ViewHolder{

        public ChapterViewHolder(@NonNull View itemView) {
            // item view => onCreateViewHolder 에서 넘겨준 뷰.
            super(itemView);

        }

    }
}
