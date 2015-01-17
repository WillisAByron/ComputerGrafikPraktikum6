package computergraphics.hlsvis.hls;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import computergraphics.hlsvis.rabbitmq.IMessageCallback;
import computergraphics.hlsvis.rabbitmq.RabbitMqCommunication;

public class TransportOrderQueue implements IMessageCallback {

	private RabbitMqCommunication transportOrderQueue = new RabbitMqCommunication(HlsConstants.FRACHTAUFTRAG_QUEUE,
			"win-devel.informatik.haw-hamburg.de", "CGTeams", "Rwj9joAi");

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
//		System.out.println(message);
		if (message.contains("{")) {
			TransportOrder to = new TransportOrder();
			to.fromJson(message);
			if (!toq.contains(to)) {
				toq.add(to);
				Collections.sort(toq);
			}
		}
//		System.out.println("=========================================================");
//		for (TransportOrder transportOrder : toq) {
//			System.out.println(transportOrder.getDeliveryNumber() + " Start: " + transportOrder.getStartTime());
//		}
//		System.out.println("Listen größe: " + toq.size());

	}
	
	public List<TransportOrder> getList(){
		return this.toq;
	}

}
