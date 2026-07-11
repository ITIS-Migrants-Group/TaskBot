CREATE TABLE notification (
    id UUID PRIMARY KEY,
    owner_id BIGINT NOT NULL,
    title VARCHAR(1023) NOT NULL,
    task_id UUID,
    type VARCHAR(50) NOT NULL,
    notify_at TIMESTAMP NOT NULL,
    period INTERVAL,
    is_active BOOLEAN NOT NULL DEFAULT true
)