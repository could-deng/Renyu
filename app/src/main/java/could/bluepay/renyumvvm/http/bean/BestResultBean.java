package could.bluepay.renyumvvm.http.bean;

import java.util.List;

/**
 * Best平台返回的数据
 */

public class BestResultBean {

    /**
     * status : 200
     * date : [{"ccy":"THB","sum_numbers":4291,"sum_price":150703,"create_time":"2017-53","max_time":"2017-12-31"},{"ccy":"THB","sum_numbers":4426,"sum_price":165584,"create_time":"2017-53","max_time":"2018-01-01"},{"ccy":"THB","sum_numbers":4906,"sum_price":187281,"create_time":"2017-53","max_time":"2018-01-02"},{"ccy":"THB","sum_numbers":4852,"sum_price":189371,"create_time":"2017-53","max_time":"2018-01-03"},{"ccy":"THB","sum_numbers":6657,"sum_price":279180,"create_time":"2017-53","max_time":"2018-01-04"},{"ccy":"THB","sum_numbers":4658,"sum_price":170843,"create_time":"2017-53","max_time":"2018-01-05"}]
     */

    private int status;
    private List<DateBean> date;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DateBean> getDate() {
        return date;
    }

    public void setDate(List<DateBean> date) {
        this.date = date;
    }

    public static class DateBean {
        /**
         * ccy : THB
         * sum_numbers : 4291
         * sum_price : 150703
         * create_time : 2017-53
         * max_time : 2017-12-31
         */

        private String ccy;
        private int sum_numbers;
        private int sum_price;
        private String create_time;
        private String max_time;

        public String getCcy() {
            return ccy;
        }

        public void setCcy(String ccy) {
            this.ccy = ccy;
        }

        public int getSum_numbers() {
            return sum_numbers;
        }

        public void setSum_numbers(int sum_numbers) {
            this.sum_numbers = sum_numbers;
        }

        public int getSum_price() {
            return sum_price;
        }

        public void setSum_price(int sum_price) {
            this.sum_price = sum_price;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getMax_time() {
            return max_time;
        }

        public void setMax_time(String max_time) {
            this.max_time = max_time;
        }
    }
}
