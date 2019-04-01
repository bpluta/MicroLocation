package sample;

import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class AntennaLog {
    public List<AntennaLogLine> logList;

    public List<AntennaLogLine> AntennaLog() {
        this.logList = new ArrayList<>();
        return this.logList;
    }

    private AntennaLogLine appendToLog() {
        return this.logList.get(0);
    }

    public List<AntennaLogLine> importLogs(String path) {
        return this.logList;
    }

    public List<AntennaLogLine> getLog() {
        return this.logList;
    }

    public List<AntennaLogLine> addLog(String path) throws IOException {
        this.logList = new LinkedList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            logList.add(new AntennaLogLine(line));
        }
        return this.logList;
    }

    public List<AntennaLogLine> printLog() {
        for (AntennaLogLine aLogList : this.logList) {
            System.out.println(aLogList.getLogLine());
        }
        return this.logList;
    }

    public ArrayList<AntennaLogLine> getAll(Date time) {
        ArrayList<AntennaLogLine> logLines = new ArrayList<>();
        for (AntennaLogLine logLine:this.logList) {
            if (logLine.timeStamp.equals(time)) {
                logLines.add(logLine);
            }
        }
        return logLines;
    }

    public ArrayList<Date> getAllDates() {
        ArrayList<Date> dates = new ArrayList<>();
        ArrayList<AntennaLogLine> logLines = new ArrayList<>(this.logList);
        Iterator<AntennaLogLine> it = logLines.iterator();
        AntennaLogLine itLine;

        do {
            Date earliest = logLines.get(0).timeStamp;
            int earliestIndex = 0;
            int i=0;
            while (it.hasNext()) {
                itLine=it.next();
                if (itLine.timeStamp.before(earliest)) {
                    earliest = new Date(itLine.timeStamp.getTime());
                    earliestIndex = i;
                }
                i++;
            }
            Boolean isInDates = false;
            for (Date date : dates) {
                if (date.equals(logLines.get(earliestIndex).timeStamp)) isInDates = true;
            }
            if (!isInDates) dates.add(new Date(logLines.get(earliestIndex).timeStamp.getTime()));
            logLines.remove(earliestIndex);
            it = logLines.iterator();
        } while (logLines.size()>0);

        return dates;
    }
}
