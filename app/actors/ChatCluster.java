package actors;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.avaje.ebean.Ebean;
import messages.NotifyAll;
import messages.RoomMessage;
import messages.UserConnection;
import models.Message;
import play.PlayInternal;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;

public class ChatCluster extends UntypedActor {

	public static ActorRef CHAT_CLUSTER;
	private final Cluster cluster;
	private final ActorRef chatRoom;

	public ChatCluster() {
		cluster = Cluster.get(getContext().system());
		chatRoom = getContext().actorOf(Props.create(ChatRoom.class));
	}

	public static void userConnetion(UserConnection connection) {
		CHAT_CLUSTER.tell(connection, null);
	}

	public static void sendMessage(NotifyAll message) {
		CHAT_CLUSTER.tell(message, null);
	}

	@Override
	public void preStart() {
		cluster.subscribe(getSelf(),
				ClusterEvent.initialStateAsEvents(),
				ClusterEvent.MemberEvent.class,
				ClusterEvent.UnreachableMember.class);
	}

	@Override
	public void postStop() {
		cluster.unsubscribe(getSelf());
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof UserConnection) {
			UserConnection userConnection = (UserConnection) message;
			chatRoom.tell(userConnection, getSelf());
		} else if (message instanceof RoomMessage) {
			RoomMessage roomMessage = (RoomMessage) message;
			chatRoom.tell(roomMessage, getSelf());
		} else if (message instanceof NotifyAll) {
			RoomMessage roomMessage = ((NotifyAll) message).message;

			PlayInternal.logger().info("---------------> mensaje: " + roomMessage.message +
			" Usuario: "+ roomMessage.userName);

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmdd H:mm:ss");
			Date date = new Date();
			Ebean.save(new Message(roomMessage.message,roomMessage.userName,roomMessage.roomName, dateFormat.format(date)));

			CHAT_CLUSTER.tell(roomMessage, getSelf());
		}


	}
}
