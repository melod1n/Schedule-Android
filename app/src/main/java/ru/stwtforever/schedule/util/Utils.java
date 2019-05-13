package ru.stwtforever.schedule.util;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.database.*;
import android.graphics.*;
import android.net.*;
import android.provider.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v4.graphics.*;
import android.support.v7.app.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import ru.stwtforever.schedule.*;
import ru.stwtforever.schedule.common.*;
import ru.stwtforever.schedule.io.*;

import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;

public class Utils {

	public static void setFirstLaunch(boolean first) {
		AppGlobal.preferences.edit().putBoolean("first_launch", first).apply();
	}

	public static boolean isFirstLaunch() {
		return AppGlobal.preferences.getBoolean("first_launch", true);
	}
	
	public static void restart(Activity activity, Intent extras, boolean anim) {
		Intent intent = new Intent(activity, activity.getClass());
		if (extras != null)
			intent.putExtras(extras);
		
		activity.startActivity(intent);
		activity.finish();
		
		if (anim)
			activity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
	}
	
	public static void restart(Activity activity, boolean anim) {
		restart(activity, null, anim);
	}
	
	public static String multiply(String s, String a, int count) {
		StringBuilder builder = new StringBuilder(s);
		
		for (int i = 0; i < count; i++) {
			builder.append(a);
		}
		
		return builder.toString();
	}
	
	public static String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = AppGlobal.context.getContentResolver().query(contentUri, proj, null, null, null);
      
		if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
	
	public static @ColorInt int getRandomColor() {
		int red = (int) (Math.random() * 256);
		int green = (int) (Math.random() * 256);
		int blue = (int) (Math.random() * 256);
		
		return Color.rgb(red, green, blue);
	}
	
	public static void createFile(File path, String name, String trace) {
        File file = new File(path, name);
        try {
            FileStreams.write(trace, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static boolean isMatch(String s, String regex) {
		return Pattern.matches(regex, s);
	}
	
	public static boolean isLight(@ColorInt int color) {
		return getLuminance(color) >= 0.5;
	}
	
	public static boolean isDark(@ColorInt int color) {
		return getLuminance(color) < 0.5;
	}
	
	public static double getLuminance(@ColorInt int color) {
		return ColorUtils.calculateLuminance(color);
	}
	
	public static int manipulateColor(@ColorInt int color) {
		return manipulateColor(color, 0.8f);
	}
	
	public static boolean isValidHexColor(String color) {
		return Pattern.matches("^#?([a-f0-9]{6}|[a-f0-9]{3})$", color);
	}
	
	public static void copyText(String text) {
        ClipboardManager cm = (ClipboardManager) AppGlobal.context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(null, text));
    }
	
	public static int manipulateColor(@ColorInt int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
						  Math.min(r, 255),
						  Math.min(g, 255),
						  Math.min(b, 255));
    }
	
	
	public static String repeat(String s, int i) {
        StringBuilder sb = new StringBuilder(s.length() * i);
        while (i-- > 0) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static Bitmap getPreview(String fileName) {
        File image = new File(fileName);

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getPath(), bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
            return null;

        int original_size = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight : bounds.outWidth;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = original_size / 64;
        return BitmapFactory.decodeFile(image.getPath(), opts);
    }

    public static void requestUserAvatar(Activity context) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        //context.startActivityForResult(Intent.createChooser(intent, "Choose picture:"), MainActivity.REQUEST_PICK_AVATAR);
    }

    public static String leadingZero(int num) {
        return num > 9 ? String.valueOf(num) : "0" + String.valueOf(num);
    }

    public static void showNoInternetToast() {
        Toast.makeText(AppGlobal.context, "No internet connection", Toast.LENGTH_LONG).show();
    }

    public static void checkConnection() {
        if (!hasConnection()) showNoInternetToast();
    }
	
	public static boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) AppGlobal.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null &&
			cm.getActiveNetworkInfo().isAvailable() &&
			cm.getActiveNetworkInfo().isConnected());
    }
	
	public static byte[] serialize(Object source) {
        try {
            BytesOutputStream bos = new BytesOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);

            out.writeObject(source);
            out.close();
            return bos.getByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object deserialize(byte[] source) {
        if (ArrayUtil.isEmpty(source)) {
            return null;
        }

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(source);
            ObjectInputStream in = new ObjectInputStream(bis);

            Object o = in.readObject();

            in.close();
            return o;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap textAsBitmap(String text, float textSize, int textColor, int backgroundColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent();
        int width = (int) (paint.measureText(text) + 0.5f);
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        image.setHasAlpha(true);
        Canvas canvas = new Canvas(image);
        canvas.drawColor(backgroundColor);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public static Bitmap textAsBitmap(Bitmap b, String textToDraw, float textSize) {
        Bitmap newBitmap = b.copy(b.getConfig(), true);

        Canvas newCanvas = new Canvas(newBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setTextSize(textSize);

        Rect bounds = new Rect();
        paint.getTextBounds(textToDraw, 0, textToDraw.length(), bounds);
        int x = 0;
        int y = newBitmap.getHeight();

        newCanvas.drawText(textToDraw, x, y, paint);
        return b;
    }

    public static int getDarkenColor(int color) {
        float ratio = 1.0f - 0.2f;
        int a = (color >> 24) & 0xFF;
        int r = (int) (((color >> 16) & 0xFF) * ratio);
        int g = (int) (((color >> 8) & 0xFF) * ratio);
        int b = (int) ((color & 0xFF) * ratio);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int getDarkenColor(int color, float ratio) {
        int a = (color >> 24) & 0xFF;
        int r = (int) (((color >> 16) & 0xFF) * ratio);
        int g = (int) (((color >> 8) & 0xFF) * ratio);
        int b = (int) ((color & 0xFF) * ratio);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int getBrightness(int cc) {
        String color = Integer.toHexString(cc);
        String c = color.substring(1);
        int rgb = Integer.parseInt(c, 16);
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = (rgb) & 0xff;

        double luma = 0.2126 * r + 0.7152 * g + 0.0722 * b;
        return (int) luma;
    }

    public static String getStringDay(int day) {
        String[] days = AppGlobal.context.getResources().getStringArray(R.array.days);
        return days[day];
    }
	
	public static int getNumOfCurrentDay() {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK); 

		switch (day) {
			case Calendar.SUNDAY:
			case Calendar.MONDAY:
				day = 0;
				break;
			case Calendar.TUESDAY:
				day = 1;
				break;
			case Calendar.WEDNESDAY:
				day = 2;
				break;
			case Calendar.THURSDAY:
				day = 3;
				break;
			case Calendar.FRIDAY:
				day = 4;
				break;
			case Calendar.SATURDAY:
				day = 5;
				break;
		}
		
		return day;
	}

    public static boolean isInternetOn(Context c) {
        ConnectivityManager connec = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
			connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
			connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
			connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
			connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
			connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public static NotificationManager showNotification(Context context, String title, String text, int small_icon, int large_icon, int notify_id) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
																0, notificationIntent,
																PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = context.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentIntent(contentIntent)
			.setSmallIcon(small_icon)
			.setContentTitle(title)
			//.setColor(ThemeManager.getCurrentTheme().getPrimary())
			.setContentText(text)
			.setLargeIcon(BitmapFactory.decodeResource(res, large_icon))
			.setWhen(System.currentTimeMillis())
			.setAutoCancel(true);

        NotificationManager notificationManager =
			(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notify_id, builder.build());

        return notificationManager;
    }
	
	public static View.OnClickListener getDismissClick(final AlertDialog dialog) {
		return new View.OnClickListener() {

			@Override
			public void onClick(View p1) {
				dialog.dismiss();
			}
		};
	};
	
	public static boolean[] isTimeValueValid(String h, String m) {
		boolean ho = false;
		boolean mi = false;

		if (h.length() > 0 && Integer.parseInt(h) < 24)
			ho = true;

		if (m.length() > 0 && Integer.parseInt(m) < 60)
			mi = true;

		return new boolean[] {ho, mi};
	}
}
