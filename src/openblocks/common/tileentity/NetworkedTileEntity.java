package openblocks.common.tileentity;

import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import openblocks.sync.ISyncHandler;
import openblocks.sync.ISyncableObject;
import openblocks.sync.SyncMap;
import openblocks.sync.SyncMapTile;

public abstract class NetworkedTileEntity extends OpenTileEntity implements
		ISyncHandler {

	protected SyncMapTile syncMap = new SyncMapTile();

	public void addSyncedObject(ISyncableObject obj) {
		syncMap.put(obj);
	}

	public void sync(boolean syncMeta) {
		if (syncMeta) {
			super.sync();
		}
		syncMap.sync(worldObj, this, xCoord, yCoord, zCoord);
	}

	@Override
	public void sync() {
		sync(true);
	}

	@Override
	public void writeIdentifier(DataOutputStream dos) throws IOException {
		dos.writeInt(worldObj.provider.dimensionId);
		dos.writeInt(xCoord);
		dos.writeInt(yCoord);
		dos.writeInt(zCoord);
	}

	@Override
	public SyncMap getSyncMap() {
		return syncMap;
	}

	@Override
	public Packet getDescriptionPacket() {
		return syncMap.getDescriptionPacket(this);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		syncMap.handleTileDataPacket(pkt);
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}

}
