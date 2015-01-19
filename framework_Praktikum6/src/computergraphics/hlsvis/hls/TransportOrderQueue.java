package computergraphics.hlsvis.hls;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import computergraphics.hlsvis.rabbitmq.IMessageCallback;

public class TransportOrderQueue implements IMessageCallback {

	@SuppressWarnings("serial")
	private List<TransportOrder> toq = new LinkedList<TransportOrder>() {

		@Override
		public boolean contains(Object o) {
			Object[] thisTO = this.toArray();
			for (Object transportOrder : thisTO) {
				if (((TransportOrder) transportOrder).getDeliveryNumber() == ((TransportOrder) o).getDeliveryNumber()
						&& ((TransportOrder) transportOrder).getOrderNumber() == ((TransportOrder) o).getOrderNumber()) {
					return true;
				}
			}
			return false;
		}
	};

	@Override
	public void messageReceived(String message) {
		if (message.contains("{")) {
			TransportOrder to = new TransportOrder();
			to.fromJson(message);
			if (!toq.contains(to)) {
				toq.add(to);
				Collections.sort(toq);
			}
		}
	}
	
	public List<TransportOrder> getList(){
		return this.toq;
	}

}
