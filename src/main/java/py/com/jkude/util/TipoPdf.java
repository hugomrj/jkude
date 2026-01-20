package py.com.jkude.util;


public enum TipoPdf {

    KUDE {
        @Override
        public String jrxml() {
            return "kude.jrxml";
        }
    },
    TICKET {
        @Override
        public String jrxml() {
            return "ticket.jrxml";
        }
    };

    public abstract String jrxml();
}
