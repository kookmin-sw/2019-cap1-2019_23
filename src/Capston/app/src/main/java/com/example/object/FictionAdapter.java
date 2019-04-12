package com.example.object;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.capston.R;

import java.util.List;

public class FictionAdapter extends RecyclerView.Adapter<FictionAdapter.FictionViewHolder> {

    // Adapter란 Data 관리를 도와주고 list(RecyclerView)의 갱신을 관리하는 역활을한다.

    //리스트
    private List<Chapter> fictionList;

    // 생성자.
    public FictionAdapter(List<Chapter> fictionList) {
        this.fictionList = fictionList;
    }

    @NonNull
    @Override
    public FictionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // onCreateViewHolder  viewGroup에서 context를 얻어와서 인플레이트 한다
        View list = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fiction_item_recycler_view, viewGroup, false);

        return new FictionViewHolder(list);
    }

    @Override
    public void onBindViewHolder(@NonNull FictionViewHolder fictionViewHolder, int i) {
        // i = position
        // List에서 하나씩 꺼내서 item의 각각의 view에 넣는다.
        //뷰홀더 참조후 넣어준다.각각의 뷰에 할당.
    }

    @Override
    public int getItemCount() {
        return fictionList.size();
    }

    //객체를 보여줄 뷰를 보관하는 내부 클래스.
    static class FictionViewHolder extends RecyclerView.ViewHolder{

        public FictionViewHolder(@NonNull View itemView) {
            // item view => onCreateViewHolder 에서 넘겨준 뷰.
            super(itemView);

        }

    }
}