package broboss64.totemtastic.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReviveState extends PersistentState {
    private final Map<UUID, Integer> reviveCounts = new HashMap<>();
    public static final String reviveKey = "timesRevived";

    public ReviveState() {
    }

    public static ReviveState fromNbt(NbtCompound nbt) {
        ReviveState state = new ReviveState();
        state.readNbt(nbt);
        return state;
    }

    public void readNbt(NbtCompound nbt) {
        reviveCounts.clear();
        for (String key : nbt.getKeys()) {
            reviveCounts.put(UUID.fromString(key), nbt.getInt(key));
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        for (Map.Entry<UUID, Integer> entry : reviveCounts.entrySet()) {
            nbt.putInt(entry.getKey().toString(), entry.getValue());
        }
        return nbt;
    }

    public int getReviveCount(UUID uuid) {
        return reviveCounts.getOrDefault(uuid, 0);
    }

    public void setReviveCount(UUID uuid, int count) {
        reviveCounts.put(uuid, count);
        markDirty();
    }

    public static ReviveState get(ServerWorld world) {
        PersistentStateManager manager = world.getPersistentStateManager();
        return manager.getOrCreate(ReviveState::fromNbt, ReviveState::new, reviveKey);
    }

}
