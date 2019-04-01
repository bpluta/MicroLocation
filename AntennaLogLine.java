package sample;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class AntennaLogLine {
    public int RSSI;
    public String MAC;
    public Date timeStamp;
    public short channel;

    private final String timePattern="yyyy-MM-dd'T'HH:mm:ss";
    private final String delimiter=",";

    AntennaLogLine(String time, String MAC, String RSSI, String channel) throws IOException{
        addTime(time);
        addMAC(MAC);
        addRSSI(RSSI);
        addChannel(channel);
    }

    AntennaLogLine(String logLine) throws IOException {
        String[] splitLogLine = logLine.split(delimiter);
        if (splitLogLine.length!=4) {
            throw new IOException("Wrong antenna log file format. Each line in the antenna log file must be in the following romat: TIME_STAMP,MAC,RSSI,CHANNEL");
        }
        addTime(splitLogLine[0]);
        addMAC(splitLogLine[1]);
        addRSSI(splitLogLine[2]);
        addChannel(splitLogLine[3]);
    }

    private String addMAC(String MAC) throws IOException {
        if (MAC.length()!=12 && MAC.length()!=17) {
            throw new IOException("MAC format error: MAC address must be 48 bit HEX number without any separators or with one-character separator separating every 8 bits of HEX digits");
        }
        StringBuilder tmp = new StringBuilder();
        String upperCaseMAC = MAC.toUpperCase();

        if (MAC.length()==12) {
            for (int i = 0; i < upperCaseMAC.length(); i++) {

                if (upperCaseMAC.charAt(i) < 48 || (upperCaseMAC.charAt(i) > 57 && upperCaseMAC.charAt(i) < 65) || upperCaseMAC.charAt(i) > 70) {
                    throw new IOException("MAC format error: NaN");
                }
                tmp.append(upperCaseMAC.charAt(i));
                if (i%2==1 && (i+1)!=upperCaseMAC.length()) tmp.append(":");
            }
        }
        else if (MAC.length()==17) {
            for (int i = 0; i < upperCaseMAC.length(); i++) {
                if (i % 3 != 2) {
                    if (upperCaseMAC.charAt(i) < 48 || (upperCaseMAC.charAt(i) > 57 && upperCaseMAC.charAt(i) < 65) || upperCaseMAC.charAt(i) > 70) {
                        throw new IOException("MAC format error: NaN");
                    }
                    tmp.append(upperCaseMAC.charAt(i));
                } else {
                    if ((upperCaseMAC.charAt(i) >= 65 && upperCaseMAC.charAt(i) <= 90) || (upperCaseMAC.charAt(i) >= 48 && upperCaseMAC.charAt(i) <= 57)) {
                        throw new IOException("MAC format error: MAC address must be 48 bit HEX number without any separators or with one-character separator separating every 8 bits of HEX digits");
                    }
                    tmp.append(':');
                }
            }
        }
        this.MAC = tmp.toString();
        return this.MAC;
    }

    private int addRSSI(String RSSI) throws IOException {
        int tmp = 0, factor=1;
        for (int i=RSSI.length()-1; i>=0; i--) {
            if (i==0) {
                if (RSSI.charAt(i)!='-') throw new IOException("RSSI format error: RSSI must be negative value");
                tmp=-tmp;
            }
            else if (RSSI.charAt(i)<48 || RSSI.charAt(i)>57) throw new IOException("RSSI format error: NaN");
            else tmp+=(RSSI.charAt(i)-48)*factor;
            factor*=10;
        }
        this.RSSI=tmp;
        return this.RSSI;
    }

    private int addRSSI(int RSSI) throws IOException {
        if (RSSI>0) throw new IOException("RSSI format error: RSSI must be negative value");
        this.RSSI = RSSI;
        return this.RSSI;
    }

    private int addChannel(String channel) throws IOException {
        short tmp = 0;
        int factor=1;
        for (int i=channel.length()-1; i>=0; i--) {
            if (i==0) {
                if (channel.charAt(i)=='-') throw new IOException("RSSI format error: RSSI must be positive value");
            }
            if (channel.charAt(i)<48 || channel.charAt(i)>57) throw new IOException("RSSI format error: NaN");
            else tmp+=(channel.charAt(i)-48)*factor;
            factor*=10;
        }
        this.channel=tmp;
        return this.channel;
    }

    private int addChannel(short channel) throws IOException {
        if (channel<1) throw new IOException("Channel format error: channel number must be greater than 0");
        this.channel = channel;
        return this.channel;
    }

    private Date addTime(String time) throws IOException {
        SimpleDateFormat parseFormat = new SimpleDateFormat(this.timePattern);
        try {
            this.timeStamp = parseFormat.parse(time);
        } catch (ParseException e) {
            throw new IOException(e.getMessage()+" - Entered date must be in this format: \"" + this.timePattern.replaceAll("\\'", "")+"\"");
        }
        return this.timeStamp;
    }

    public String getFullDate() {
        return this.timeStamp.toString();
    }

    public String getDate() {
        SimpleDateFormat displayPattern = new SimpleDateFormat("yyyy'.'MM'.'dd' 'HH:mm:ss");
        return displayPattern.format(this.timeStamp);
    }

    public String getTime() {
        SimpleDateFormat displayPattern = new SimpleDateFormat("HH:mm:ss");
        return displayPattern.format(this.timeStamp);
    }

    public String getMAC() {
        return this.MAC;
    }

    public int getRSSI() {
        return this.RSSI;
    }

    public int getChannel() {
        return this.channel;
    }

    public String getLogLine() {
        return getDate() + " " + getMAC() + " " + getRSSI() + " " + getChannel();
    }
}
