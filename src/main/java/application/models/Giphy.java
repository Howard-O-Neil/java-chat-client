package application.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Giphy {
  // import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
  // import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
  /*
   * ObjectMapper om = new ObjectMapper(); Root root = om.readValue(myJsonString),
   * Root.class);
   */
  public class Original {
    public String height;
    public String width;
    public String size;
    public String url;
    public String mp4_size;
    public String mp4;
    public String webp_size;
    public String webp;
    public String frames;
    public String hash;
  }

  public class Downsized {
    public String height;
    public String width;
    public String size;
    public String url;
  }

  public class DownsizedLarge {
    public String height;
    public String width;
    public String size;
    public String url;
  }

  public class DownsizedMedium {
    public String height;
    public String width;
    public String size;
    public String url;
  }

  public class DownsizedSmall {
    public String height;
    public String width;
    public String mp4_size;
    public String mp4;
  }

  public class DownsizedStill {
    public String height;
    public String width;
    public String size;
    public String url;
  }

  public class FixedHeight {
    public String height;
    public String width;
    public String size;
    public String url;
    public String mp4_size;
    public String mp4;
    public String webp_size;
    public String webp;
  }

  public class FixedHeightDownsampled {
    public String height;
    public String width;
    public String size;
    public String url;
    public String webp_size;
    public String webp;
  }

  public class FixedHeightSmall {
    public String height;
    public String width;
    public String size;
    public String url;
    public String mp4_size;
    public String mp4;
    public String webp_size;
    public String webp;
  }

  public class FixedHeightSmallStill {
    public String height;
    public String width;
    public String size;
    public String url;
  }

  public class FixedHeightStill {
    public String height;
    public String width;
    public String size;
    public String url;
  }

  public class FixedWidth {
    public String height;
    public String width;
    public String size;
    public String url;
    public String mp4_size;
    public String mp4;
    public String webp_size;
    public String webp;
  }

  public class FixedWidthDownsampled {
    public String height;
    public String width;
    public String size;
    public String url;
    public String webp_size;
    public String webp;
  }

  public class FixedWidthSmall {
    public String height;
    public String width;
    public String size;
    public String url;
    public String mp4_size;
    public String mp4;
    public String webp_size;
    public String webp;
  }

  public class FixedWidthSmallStill {
    public String height;
    public String width;
    public String size;
    public String url;
  }

  public class FixedWidthStill {
    public String height;
    public String width;
    public String size;
    public String url;
  }

  public class Looping {
    public String mp4_size;
    public String mp4;
  }

  public class OriginalStill {
    public String height;
    public String width;
    public String size;
    public String url;
  }

  public class OriginalMp4 {
    public String height;
    public String width;
    public String mp4_size;
    public String mp4;
  }

  public class Preview {
    public String height;
    public String width;
    public String mp4_size;
    public String mp4;
  }

  public class PreviewGif {
    public String height;
    public String width;
    public String size;
    public String url;
  }

  public class PreviewWebp {
    public String height;
    public String width;
    public String size;
    public String url;
  }

  public class Hd {
    public String height;
    public String width;
    public String mp4_size;
    public String mp4;
  }

  public class _480wStill {
    public String height;
    public String width;
    public String size;
    public String url;
  }

  public class Images {
    public Original original;
    public Downsized downsized;
    public DownsizedLarge downsized_large;
    public DownsizedMedium downsized_medium;
    public DownsizedSmall downsized_small;
    public DownsizedStill downsized_still;
    public FixedHeight fixed_height;
    public FixedHeightDownsampled fixed_height_downsampled;
    public FixedHeightSmall fixed_height_small;
    public FixedHeightSmallStill fixed_height_small_still;
    public FixedHeightStill fixed_height_still;
    public FixedWidth fixed_width;
    public FixedWidthDownsampled fixed_width_downsampled;
    public FixedWidthSmall fixed_width_small;
    public FixedWidthSmallStill fixed_width_small_still;
    public FixedWidthStill fixed_width_still;
    public Looping looping;
    public OriginalStill original_still;
    public OriginalMp4 original_mp4;
    public Preview preview;
    public PreviewGif preview_gif;
    public PreviewWebp preview_webp;
    public Hd hd;
    @JsonProperty("480w_still")
    public _480wStill _480w_still;
  }

  public class User {
    public String avatar_url;
    public String banner_image;
    public String banner_url;
    public String profile_url;
    public String username;
    public String display_name;
    public String description;
    public String instagram_url;
    public String website_url;
    public boolean is_verified;
  }

  public class Onload {
    public String url;
  }

  public class Onclick {
    public String url;
  }

  public class Onsent {
    public String url;
  }

  public class Analytics {
    public Onload onload;
    public Onclick onclick;
    public Onsent onsent;
  }

  public class Root {
    public String type;
    public String id;
    public String url;
    public String slug;
    public String bitly_gif_url;
    public String bitly_url;
    public String embed_url;
    public String username;
    public String source;
    public String title;
    public String rating;
    public String content_url;
    public String source_tld;
    public String source_post_url;
    public int is_sticker;
    public String import_datetime;
    public String trending_datetime;
    public Images images;
    public User user;
    public String analytics_response_payload;
    public Analytics analytics;
  }
}
