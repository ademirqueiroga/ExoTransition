// Generated by view binder compiler. Do not edit!
package com.ademir.exotransitionin.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import com.ademir.exotransitionin.R;
import com.google.android.exoplayer2.ui.PlayerView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentPlayerBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final PlayerView playerView;

  @NonNull
  public final TextView subtitle;

  @NonNull
  public final TextView title;

  private FragmentPlayerBinding(@NonNull ConstraintLayout rootView, @NonNull PlayerView playerView,
      @NonNull TextView subtitle, @NonNull TextView title) {
    this.rootView = rootView;
    this.playerView = playerView;
    this.subtitle = subtitle;
    this.title = title;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentPlayerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentPlayerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_player, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentPlayerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    String missingId;
    missingId: {
      PlayerView playerView = rootView.findViewById(R.id.player_view);
      if (playerView == null) {
        missingId = "playerView";
        break missingId;
      }

      TextView subtitle = rootView.findViewById(R.id.subtitle);
      if (subtitle == null) {
        missingId = "subtitle";
        break missingId;
      }

      TextView title = rootView.findViewById(R.id.title);
      if (title == null) {
        missingId = "title";
        break missingId;
      }

      return new FragmentPlayerBinding((ConstraintLayout) rootView, playerView, subtitle, title);
    }
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
