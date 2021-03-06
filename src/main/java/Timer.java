import java.util.Calendar;

public class Timer implements Mode {

    private Calendar timerTime;
    private Calendar rsvTime;

    private int status; //0: Stopped, 1: Continued, 2: Setting
    private int currSection = 0; // 0: Second, 1: Minute, 2: Hour

    public Timer(){
        this.timerTime = Calendar.getInstance();
        this.timerTime.set(1970,0,1,0,0,0);
        this.timerTime.set(Calendar.MILLISECOND, 0);

        this.rsvTime = Calendar.getInstance();
        this.rsvTime.set(1970,0,1,0,0,0);
        this.rsvTime.set(Calendar.MILLISECOND, 0);

        this.status = 0;
        this.currSection = 0;
    }

    public Calendar getTimerTime(){ return this.timerTime; }
    public void setTimerTime(Calendar timerTime){ this.timerTime = timerTime; }
    public Calendar getRsvTime(){ return this.rsvTime; }
    public void setRsvTime(Calendar rsvTime) { this.rsvTime = rsvTime; }
    public int getStatus(){ return this.status; }
    public void setStatus(int status) { this.status = status; }
    public int getCurrSection(){ return this.currSection; }
    public void setCurrSection(int currSection){ this.currSection = currSection; }

    public void requestTimerTime(){
        if(this.status == 0) // 0: Stopped -> 2: Setting
            this.changeStatus(2);
    }
    public void requestNextTimerTimeSection(){
        if(++this.currSection == 3)
            this.currSection = 0;
    }
    public void requestIncreaseTimerTimeSection(){
        switch(this.currSection){
            case 0 :
                this.rsvTime.add(Calendar.MILLISECOND, 1000);
                if(this.rsvTime.get(Calendar.MILLISECOND) == 0)
                    this.rsvTime.add(Calendar.MINUTE, -1);
                break;

            case 1:
                this.rsvTime.add(Calendar.MINUTE, 1);
                if(this.rsvTime.get(Calendar.MINUTE) == 0)
                    this.rsvTime.add(Calendar.HOUR_OF_DAY, -1);
                break;

            case 2:
                this.rsvTime.add(Calendar.HOUR_OF_DAY, 1);
                if(this.rsvTime.get(Calendar.HOUR_OF_DAY) == 0)
                    this.rsvTime.add(Calendar.DATE, -1);
                break;

            default:
                break;
        }
    }
    public void requestDecreaseTimerTimeSection(){
        switch(this.currSection){
            case 0 :
                this.rsvTime.add(Calendar.SECOND, -1);
                if(this.rsvTime.get(Calendar.SECOND) == 59)
                    this.rsvTime.add(Calendar.MINUTE, 1);
                break;

            case 1:
                this.rsvTime.add(Calendar.MINUTE, -1);
                if(this.rsvTime.get(Calendar.MINUTE) == 59)
                    this.rsvTime.add(Calendar.HOUR, 1);
                break;

            case 2:
                this.rsvTime.add(Calendar.HOUR, -1);
                break;

            default:
                break;
        }
    }

    public void changeStatus(int i){
        if(-1 < i && i < 3)
            this.status = i;
    }
    public void requestResetTimer(){
        if(this.status == 0) // 0: Stopped
            this.setTimerReservatedTime();
    }
    private void setTimerReservatedTime(){
        this.timerTime.set(Calendar.MILLISECOND, 0);
        this.timerTime.set(
                this.rsvTime.get(Calendar.YEAR),
                this.rsvTime.get(Calendar.MONTH),
                this.rsvTime.get(Calendar.DATE),
                this.rsvTime.get(Calendar.HOUR_OF_DAY),
                this.rsvTime.get(Calendar.MINUTE),
                this.rsvTime.get(Calendar.SECOND)

        );
    }
    //private void setTimerTime(){}
    public void ringOff(){}
    private void startRingingTimer(){}
    public void realTimeTimerTask(){
        if(this.status == 1){
            this.timerTime.add(Calendar.MILLISECOND, -10);
            if(this.timerTime.getTimeInMillis() == 0){
                this.changeStatus(0);
                // Beep
            }
        }

    }
    public void showTimer(){}
    public void requestExitSetTimerTime(){
        this.changeStatus(0);
    }
}
