CREATE TABLE ingredients
(
    id         BIGSERIAL PRIMARY KEY,
    name       TEXT    NOT NULL UNIQUE,
    vegetarian BOOLEAN NOT NULL
);

CREATE TABLE recipes
(
    id                 BIGSERIAL PRIMARY KEY,
    name               TEXT NOT NULL,
    number_of_servings INT  NOT NULL
);

CREATE TABLE recipe_ingredients
(
    id            BIGSERIAL PRIMARY KEY,
    recipe_id     BIGSERIAL        NOT NULL REFERENCES recipes (id),
    ingredient_id BIGSERIAL        NOT NULL REFERENCES ingredients (id),
    label         TEXT             NOT NULL,
    value         DOUBLE PRECISION NOT NULL,
    unit          TEXT             NOT NULL,
    sort_order    INT              NOT NULL,
    UNIQUE (recipe_id, ingredient_id, label)
);

CREATE TABLE recipe_instructions
(
    id          BIGSERIAL PRIMARY KEY,
    recipe_id   BIGSERIAL NOT NULL REFERENCES recipes (id),
    sort_order  INT       NOT NULL,
    instruction TEXT      NOT NULL,
    UNIQUE (recipe_id, sort_order)
);
