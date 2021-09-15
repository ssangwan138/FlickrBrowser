package deepaksangwan.example.flickrbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.ui.AppBarConfiguration;

import com.squareup.picasso.Picasso;

import deepaksangwan.example.flickrbrowser.databinding.ActivityPhotoDetailBinding;

public class PhotoDetailActivity extends BaseActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPhotoDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPhotoDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activateToolbar(true);

        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);

        if(photo != null){
            TextView author = (TextView)findViewById(R.id.photo_author);
            author.setText(photo.getAuthor());
            TextView title = (TextView) findViewById(R.id.photo_title);
            title.setText(photo.getTitle());
            TextView tags= (TextView) findViewById(R.id.photo_tags);
            tags.setText(photo.getTag());

            ImageView image = (ImageView)findViewById(R.id.photo_image);
            Picasso.get().load(photo.getLink())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(image);
        }

    }


}