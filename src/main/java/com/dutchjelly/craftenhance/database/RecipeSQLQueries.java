package com.dutchjelly.craftenhance.database;

public interface RecipeSQLQueries {

	String SELECT_RECIPE_SQL = "SELECT id, page, slot, result_slot, category, permission, matchtype, hidden, check_partial_match, oncraftcommand, result_item_id, shapeless FROM recipes WHERE id = ?";
	String CHECK_ITEM_EXISTENCE_SQL = "SELECT slot FROM items WHERE recipe_id = ? AND slot = ?;";
	String CHECK_INGREDIENT_EXISTENCE_SQL = "SELECT 1 FROM ingredients WHERE recipe_id = ? AND item_id = ?";
	String SELECT_ITEM_SQL = "SELECT recipe_id, slot FROM items WHERE recipe_id = ? AND slot = ?;";
	String SELECT_RECIPE_JOIN_SQL = "SELECT r.*, i.item_nbt AS result_nbt " +
			"FROM recipes r " +
			"JOIN items i ON r.result_item_id = i.slot" +
			"WHERE r.id = ?;";
	String SELECT_INGREDIENTS_SQL = "SELECT i.item_nbt, i.slot " +
			"FROM items i " +
			"WHERE i.recipe_id = ? " +
			"AND i.slot BETWEEN 0 AND 8 " +
			"ORDER BY i.slot;";
	String SELECT_WORLDS_SQL = "SELECT world FROM allowed_worlds WHERE recipe_id = ?;";


	String UPDATE_ITEM_SQL = "UPDATE items SET item_nbt = ? WHERE recipe_id = ? and slot = ?;";
	String UPDATE_INGREDIENT_SQL = "UPDATE ingredients SET item_id = ? WHERE recipe_id = ? AND item_id = ?;";
	String UPDATE_RECIPE_SQL = "UPDATE recipes " +
			"SET page = ?, " +
			"    slot = ?, " +
			"    result_slot = ?, " +
			"    category = ?, " +
			"    permission = ?, " +
			"    matchtype = ?, " +
			"    hidden = ?, " +
			"    check_partial_match = ?, " +
			"    oncraftcommand = ?, " +
			"    result_item_id = ?, " +
			"    shapeless = ? " +
			"WHERE id = ?;";
	String UPDATE_WORLDS_SQL = "UPDATE allowed_worlds SET world = ? WHERE recipe_id = ? AND world = ?";


	String INSERT_OR_REPLACE_WORLDS_SQL = "INSERT INTO allowed_worlds (recipe_id, world) VALUES (?, ?) " +
			"ON CONFLICT(recipe_id, world) DO UPDATE SET world = excluded.world;";

	String INSERT_WORLDS_SQL = "INSERT INTO allowed_worlds (recipe_id, world) VALUES (?, ?);";
	String INSERT_ITEM_SQL = "INSERT INTO items (recipe_id, slot, name, item_nbt) VALUES (?, ?, ?, ?)";
	String INSERT_INGREDIENT_SQL = "INSERT INTO ingredients (recipe_id, item_id) VALUES (?, ?)";

	String DELETE_WORLD_SQL = "DELETE FROM allowed_worlds WHERE recipe_id = ? AND world = ?;";
}